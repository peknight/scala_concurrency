package org.learningconcurrency
package ch6

import rx.lang.scala._
import scala.concurrent.duration._
import CompositionRetry._

object Exercise3 extends App {
  val timeQuote = (for (_ <- Observable.interval(1.second)) yield randomQuote).flatten
  val sum = timeQuote.scan((0, 0)) { (x, y) => (x._1 + y.length, x._2 + 1) }.tail
  timeQuote.subscribe(log _)
  sum.subscribe(x => log(x._1.toDouble / x._2))
  Thread.sleep(20000)
}
