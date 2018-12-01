package org.learningconcurrency
package ch3

import java.util.concurrent.{ConcurrentHashMap, LinkedBlockingQueue}

import scala.collection.convert.decorateAsScala._
import scala.collection.concurrent
import java.io.File

import org.apache.commons.io.FileUtils

import scala.annotation.tailrec

class FileSystem(val root: String) {
  private val messages = new LinkedBlockingQueue[String]
  val logger = new Thread {
    setDaemon(true)
    override def run(): Unit = while (true) log(messages.take())
  }
  logger.start()
  def logMessage(msg: String): Unit = messages.offer(msg)

  val rootDir = new File(root)
  val files: concurrent.Map[String, Entry] = new ConcurrentHashMap[String, Entry]().asScala
  for (f <- FileUtils.iterateFiles(rootDir, null, false).asScala)
    files.put(f.getName, new Entry(false))

  @tailrec private def prepareForDelete(entry: Entry): Boolean = {
    val s0 = entry.state.get
    s0 match {
      case i: Idle =>
        if (entry.state.compareAndSet(s0, new Deleting)) true
        else prepareForDelete(entry)
      case c: Creating =>
        logMessage("File currently created, cannot delete."); false
      case c: Copying =>
        logMessage("File currently copied, cannot delete."); false
      case d: Deleting =>
        false
    }
  }

  def deleteFile(filename: String): Unit = {
    files.get(filename) match {
      case None =>
        logMessage(s"Path '$filename' does not exist!")
      case Some(entry) if entry.isDir =>
        logMessage(s"Path '$filename' is a directory!")
      case Some(entry) => execute {
        if (prepareForDelete(entry))
          if (FileUtils.deleteQuietly(new File(filename)))
            files.remove(filename)
      }
    }
  }

  @tailrec private def acquire(entry: Entry): Boolean = {
    val s0 = entry.state.get
    s0 match {
      case _: Creating | _: Deleting =>
        logMessage("File inaccessible, cannot copy."); false
      case i: Idle =>
        if (entry.state.compareAndSet(s0, new Copying(1))) true
        else acquire(entry)
      case c: Copying =>
        if (entry.state.compareAndSet(s0, new Copying(c.n + 1))) true
        else acquire(entry)
    }
  }

  @tailrec private def release(entry: Entry): Unit = {
    val s0 = entry.state.get
    s0  match {
      case c: Creating =>
        if (!entry.state.compareAndSet(s0, new Idle)) release(entry)
      case c: Copying =>

    }
  }

  def copyFile(src: String, dest: String): Unit = {
    files.get(src) match {
      case Some(srcEntry) if !srcEntry.isDir => execute {
        if (acquire(srcEntry)) try {
          val destEntry = new Entry(isDir = false)
          destEntry.state.set(new Creating)
          if (files.putIfAbsent(dest, destEntry) == None) try {
            FileUtils.copyFile(new File(src), new File(dest))
          } finally release(destEntry)
        } finally release(srcEntry)
      }
    }
  }
}

object FileSystem extends App {
  val fileSystem = new FileSystem(".")
  fileSystem.logMessage("Testing log")
  fileSystem.deleteFile("test.txt")
  Thread.sleep(500)
}
