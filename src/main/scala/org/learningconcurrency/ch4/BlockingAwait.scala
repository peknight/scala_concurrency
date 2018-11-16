package org.learningconcurrency.ch4

import org.learningconcurrency.learningconcurrency._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.async.Async._
import scala.concurrent.duration._
import scala.io.Source

object BlockingAwait extends App {
  val urlSpecSizeFuture = Future {
    val specUrl = "http://www.w3.org/Addressing/URL/url-spec.txt"
    Source.fromURL(specUrl).size
  }
  val urlSpecSize = Await.result(urlSpecSizeFuture, 10.seconds)
  log(s"url spec contains $urlSpecSize characters")

  val startTime = System.nanoTime
  val futures = for (_ <- 0 until 16) yield Future {
    blocking {
      Thread.sleep(1000)
    }
  }
  for (f <- futures) Await.ready(f, Duration.Inf)
  val endTime = System.nanoTime
  log(s"Total Time = ${(endTime - startTime) / 1000000} ms")
  log(s"Total CPUS = ${Runtime.getRuntime.availableProcessors}")

  def delay(n: Int): Future[Unit] = async {
    blocking { Thread.sleep(n * 1000) }
  }

  async {
    log("T-minus 1 second")
    await { delay(1) }
    log("done")
  }

  def countdown(n: Int)(f: Int => Unit): Future[Unit] = async {
    // await must not be used under a nested function
//    for (i <- n until 0) {
//      f(i)
//      await { delay(1) }
//    }
    var i = n
    while (i > 0) {
      f(i)
      await { delay(1) }
      i -= 1
    }
  }

  for (_ <- countdown(10) { n => log(s"T-minus $n seconds") }) log(s"This program is over")

  Thread.sleep(10000)
}
