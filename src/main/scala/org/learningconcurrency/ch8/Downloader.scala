package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._
import org.apache.commons.io.FileUtils

import scala.io.Source

class Downloader extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case DownloadManager.Download(url, dest) =>
      log.info(s"start download $url to $dest")
      val content = Source.fromURL(url)
      FileUtils.write(new java.io.File(dest), content.mkString)
      sender ! DownloadManager.Finished(dest)
  }
}
