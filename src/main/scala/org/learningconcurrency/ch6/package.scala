package org.learningconcurrency

import java.util.concurrent.Executor

import rx.lang.scala._

import scala.swing._
import rx.schedulers.Schedulers.{from => fromExecutor}
import javax.swing.SwingUtilities.invokeLater
import rx.lang.scala.schedulers.IOScheduler

import scala.swing.event.{ButtonClicked, ValueChanged}

package object ch6 {
  // 这个Scheduler会报错，暂时不知道为什么 NoSuchMethodException
//  val swingScheduler = new Scheduler {
//    val asJavaScheduler = fromExecutor(invokeLater _)
//  }
  // 替换成这个就好用了
  val swingScheduler: Scheduler = IOScheduler()

  implicit class ButtonOps(val self: Button) {
    def clicks = Observable.create[Unit] {
      obs =>
        self.reactions += {
          case ButtonClicked(_) => obs.onNext(())
        }
        Subscription()
    }
  }

  implicit class TextFieldOps(val self: TextField) {
    def texts = Observable.create[String] {
      obs =>
        self.reactions += {
          case ValueChanged(_) => obs.onNext(self.text)
        }
        Subscription()
    }
  }
}
