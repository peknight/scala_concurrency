package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global

object FuturesCreate extends App {
  Future { log("the future is here") }
  log("the future is coming")
  Thread.sleep(1000)
}
