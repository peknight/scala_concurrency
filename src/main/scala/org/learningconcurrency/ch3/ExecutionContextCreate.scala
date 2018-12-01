package org.learningconcurrency
package ch3

import scala.concurrent._

object ExecutionContextCreate extends App {
  val pool = new forkjoin.ForkJoinPool(2)
  val ectx = ExecutionContext.fromExecutorService(pool)
  ectx.execute(() => log("Running on the execution context again"))
  Thread.sleep(500)
}
