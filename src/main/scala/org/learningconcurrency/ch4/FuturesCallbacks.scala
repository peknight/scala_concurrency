package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.io.Source
import scala.util.{Success, Failure}

object FuturesCallbacks extends App {
  def getUrlSpec(): Future[List[String]] = Future {
    val url = "http://www.w3.org/Addressing/URL/url-spec.txt"
    val f = Source.fromURL(url)
    try f.getLines.toList finally f.close
  }

  def find(lines: List[String], keyword: String): String =
    lines.zipWithIndex collect {
      case (line, n) if line.contains(keyword) => (n, line)
    } mkString("\n")


  val urlSpec: Future[List[String]] = getUrlSpec()
//  for (lines <- urlSpec) log(find(lines, "telnet"))
//  for (lines <- urlSpec) log(find(lines, "password"))
  log("callback registered, continuing with other work")
  urlSpec onComplete {
    case Success(txt) => log(find(txt, "telnet"))
    case Failure(err) => log(s"exception occurred - $err")
  }
  Thread.sleep(10000)
}
