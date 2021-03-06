package org.learningconcurrency

import java.util.concurrent.Executor

import rx.lang.scala._
import rx.lang.scala.schedulers.IOScheduler

import scala.swing._
import scala.swing.event.{ButtonClicked, ValueChanged}

package object ch6 {
  // 这个Scheduler会报错，暂时不知道为什么 NoSuchMethodException
//  val swingScheduler = new Scheduler {
//    val asJavaScheduler = fromExecutor(invokeLater _)
//  }
  // 不过源代码是这样写的
//  val swingScheduler = new Scheduler {
//    val asJavaScheduler = rx.schedulers.Schedulers.from(new Executor {
//      def execute(r: Runnable) = javax.swing.SwingUtilities.invokeLater(r)
//    })
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
