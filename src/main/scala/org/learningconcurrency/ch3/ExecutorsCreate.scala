package org.learningconcurrency
package ch3

import scala.concurrent.forkjoin

object ExecutorsCreate extends App {
  val executor = new forkjoin.ForkJoinPool
  executor.execute {
    () => log("This task is run asynchronously.")
  }
  Thread.sleep(500)
}
