package org.learningconcurrency.ch4

import org.learningconcurrency.learningconcurrency._

import scala.concurrent._
import ExecutionContext.Implicits.global

object FuturesCreate extends App {
  Future { log("the future is here") }
  log("the future is coming")
  Thread.sleep(1000)
}
