package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object FuturesNonFatal extends App {
  val f = Future { throw new InterruptedException }
  val g = Future { throw new IllegalArgumentException }
  for (t <- f.failed) log(s"error - $t")
  for (t <- g.failed) log(s"error - $t")
  for (NonFatal(t) <- g.failed) log(s"$t is non-fatal")
  Thread.sleep(200)
}
