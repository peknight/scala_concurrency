package org.learningconcurrency
package ch9

import akka.actor._
import akka.event._
import org.learningconcurrency.ch9.FTPServerActor.{CopyFile, DeleteFile, GetFileList}

import scala.util.Try
import akka.pattern._
import scala.concurrent._
import ExecutionContext.Implicits.global

class FTPServerActor(fileSystem: FileSystem) extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case GetFileList(dir) =>
      val filesMap = fileSystem.getFileList(dir)
      val files = filesMap.map(_._2).toSeq
      sender ! files
    case CopyFile(srcpath, destpath) =>
      Future {
        Try(fileSystem.copyFile(srcpath, destpath))
      } pipeTo sender
    case DeleteFile(path) =>
      Future {
        Try(fileSystem.deleteFile(path))
      } pipeTo sender
  }
}
object FTPServerActor {
  sealed trait Command
  case class GetFileList(dir: String) extends Command
  case class CopyFile(src: String, dest: String) extends Command
  case class DeleteFile(path: String) extends Command
  def apply(fs: FileSystem) = Props(classOf[FTPServerActor], fs)
}
