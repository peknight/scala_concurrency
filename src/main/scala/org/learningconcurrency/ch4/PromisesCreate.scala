package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global

object PromisesCreate extends App {
  val p = Promise[String]
  val q = Promise[String]
  for (x <- p.future) log(s"p succeeded with '$x'")
  Thread.sleep(1000)
  p success "assigned"
  q failure new Exception("not kept")
  for (t <- q.future.failed) log(s"q failed with $t")
  Thread.sleep(1000)


}
