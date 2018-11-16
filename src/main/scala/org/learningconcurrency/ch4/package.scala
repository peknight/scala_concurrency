package org.learningconcurrency

import scala.concurrent._
import ExecutionContext.Implicits.global
import java.util.{Timer, TimerTask}

package object ch4 {
  implicit val timer = new Timer(true)
  def timeout[T](t: Long, value: => T)(implicit timer: Timer): Future[T] = {
    val p = Promise[T]
    timer.schedule(new TimerTask {
      override def run(): Unit = {
        p success value
        this.cancel()
      }
    }, t)
    p.future
  }

  implicit class FutureOps[T](val self: Future[T]) {
    def or(that: Future[T]): Future[T] = {
      val p = Promise[T]
      self onComplete { case x => p tryComplete x}
      that onComplete { case y => p tryComplete y}
      p.future
    }
  }
}
