package org.learningconcurrency
package ch4

import java.io.File

import org.apache.commons.io.monitor._
import scala.concurrent._
import ExecutionContext.Implicits.global

import java.util._


object FileAlterationObserverDemo extends App {
  def fileCreated(directory: String): Future[String] = {
    val p = Promise[String]
    val fileMonitor = new FileAlterationMonitor(1000)
    val observer = new FileAlterationObserver(directory)
    val listener = new FileAlterationListenerAdaptor {
      override def onFileCreate(file: File): Unit = try p.trySuccess(file.getName) finally fileMonitor.stop()
    }
    observer.addListener(listener)
    fileMonitor.addObserver(observer)
    fileMonitor.start()
    p.future
  }

  for (filename <- fileCreated(".")) log(s"Detected new file'$filename'")

  log(new Date)
  for (_ <- timeout(1000, ())) {
    log(new Date)
    log("Timed out!")
  }

  Thread.sleep(2000)
}
