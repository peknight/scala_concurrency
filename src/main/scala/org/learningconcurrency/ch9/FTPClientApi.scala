package org.learningconcurrency
package ch9

import akka.actor.Props
import akka.pattern._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.util
import akka.util.Timeout

import scala.util.Try

trait FTPClientApi {
  implicit val timeout: Timeout = util.Timeout(4 seconds)
  private val props = Props(classOf[FTPClientActor], timeout)
  private val system = ch8.remotingSystem("FTPClientSystem", 0)
  private val clientActor = system.actorOf(props)
  def host: String
  val connected: Future[Boolean] = {
    val f = clientActor ? FTPClientActor.Start
    f.mapTo[Boolean]
  }
  def getFileList(d: String): Future[(String, Seq[FileInfo])] = {
    val f = clientActor ? FTPServerActor.GetFileList(d)
    f.mapTo[Seq[FileInfo]].map(fs => (d, fs))
  }
  def copyFile(src: String, dest: String): Future[String] = {
    val f = clientActor ? FTPServerActor.CopyFile(src, dest)
    f.mapTo[Try[String]].map(_.get)
  }
  def deleteFile(srcpath: String): Future[String] = {
    val f = clientActor ? FTPServerActor.DeleteFile(srcpath)
    f.mapTo[Try[String]].map(_.get)
  }
}
