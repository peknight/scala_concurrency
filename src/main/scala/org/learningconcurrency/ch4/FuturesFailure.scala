package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io.Source
import scala.util.{Failure, Success, Try}

object FuturesFailure extends App {
  val urlSpec: Future[String] = Future {
    val invalidUrl = "http://www.w3.org/non-existent-url-spec.txt"
    Source.fromURL(invalidUrl).mkString
  }
  for (t <- urlSpec.failed) log(s"exception occurred -$t")
  Thread.sleep(5000)
}
