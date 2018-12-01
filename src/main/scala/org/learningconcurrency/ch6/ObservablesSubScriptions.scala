package org.learningconcurrency
package ch6

import java.io.File

import org.apache.commons.io.monitor._
import rx.lang.scala.{Observable, Subscription}

object ObservablesSubScriptions extends App {
  def modified(directory: String): Observable[String] = {
    Observable.create {
      observer =>
        val fileMonitor = new FileAlterationMonitor(1000)
        val fileObs = new FileAlterationObserver(directory)
        val fileLis = new FileAlterationListenerAdaptor {
          override def onFileChange(file: java.io.File): Unit = {
            observer.onNext(file.getName)
          }
        }
        fileObs.addListener(fileLis)
        fileMonitor.addObserver(fileObs)
        fileMonitor.start()
        Subscription { fileMonitor.stop() }
    }
  }



  val fileMonitor = new FileAlterationMonitor(1000)
  fileMonitor.start()
  def hotModified(directory: String): Observable[String] = {
    val fileObs = new FileAlterationObserver(directory)
    fileMonitor.addObserver(fileObs)
    Observable.create {
      observer =>
        val fileLis = new FileAlterationListenerAdaptor {
          override def onFileChange(file: File): Unit = {
            observer.onNext(file.getName)
          }
        }
        fileObs.addListener(fileLis)
        Subscription { fileObs.removeListener(fileLis) }
    }
  }

  log(s"starting to monitor files")
//  val sub = modified(".").subscribe(n => log(s"$n modified!"))
  val sub = hotModified(".").subscribe(n => log(s"$n modified!"))
  log(s"please modify and save a file")
  Thread.sleep(10000)
  sub.unsubscribe()
  log(s"monitoring done")

}
