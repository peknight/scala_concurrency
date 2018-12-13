package org.learningconcurrency
package ch9

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.TrueFileFilter

import scala.collection._
import scala.collection.convert.decorateAsScala._
import scala.concurrent.stm._

class FileSystem(val rootpath: String) {
  val files = TMap[String, FileInfo]()

  def init() = atomic {
    implicit txn =>
      files.clear()
      val rootDir = new File(rootpath)
      val all = TrueFileFilter.INSTANCE
      val fileIterator = FileUtils.iterateFilesAndDirs(rootDir, all, all).asScala
      for (file <- fileIterator) {
        val info = FileInfo(file)
        files(info.path) = info
      }
  }

  def getFileList(dir: String): Map[String, FileInfo] = atomic {
    implicit txn =>
      files.filter(_._2.parent == dir)
  }

  def copyFile(src: String, dest: String) = atomic {
    implicit txn =>
      val srcfile = new File(src)
      val destfile = new File(dest)
      val info = files(src)
      if (files.contains(dest)) sys.error(s"Destination exists.")
      info.state match {
        case Idle | Copying(_) =>
          files(src) = info.copy(state = info.state.inc)
          files(dest) = FileInfo.creating(destfile, info.size)
          Txn.afterCommit {_ => copyOnDisk(srcfile, destfile)}
          src
      }
  }

  private def copyOnDisk(srcfile: File, destfile: File) = {
    FileUtils.copyFile(srcfile, destfile)
    atomic {
      implicit txn =>
        val ninfo = files(srcfile.getPath)
        files(srcfile.getPath) = ninfo.copy(state = ninfo.state.dec)
        files(destfile.getPath) = FileInfo(destfile)
    }
  }

  def deleteFile(srcpath: String): String = atomic {
    implicit txn =>
      val info = files(srcpath)
      info.state match {
        case Idle =>
          files(srcpath) = info.copy(state = Deleted)
          Txn.afterCommit { _ =>
            FileUtils.forceDelete(info.toFile)
            files.single.remove(srcpath)
          }
          srcpath
      }
  }
}
