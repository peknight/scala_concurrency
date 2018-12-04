package org.learningconcurrency
package ch6

import scala.concurrent.duration._
import rx.lang.scala._

object Exercise2 extends App {
  val timer = for (time <- Observable.interval(1.seconds) if (time % 5 == 0 || time % 12 == 0) && time % 30 != 0) yield time
  timer.subscribe(log _)
  Thread.sleep(200000)
}
