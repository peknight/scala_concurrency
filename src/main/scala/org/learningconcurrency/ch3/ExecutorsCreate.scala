package org.learningconcurrency.ch3

import org.learningconcurrency.learningconcurrency._
import scala.concurrent.forkjoin

object ExecutorsCreate extends App {
  val executor = new forkjoin.ForkJoinPool
  executor.execute {
    () => log("This task is run asynchronously.")
  }
  Thread.sleep(500)
}
