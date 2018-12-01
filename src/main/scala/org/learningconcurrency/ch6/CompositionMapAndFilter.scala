package org.learningconcurrency
package ch6

import scala.concurrent.duration._
import rx.lang.scala.Observable

object CompositionMapAndFilter extends App {

  val odds = (for (n <- Observable.interval(0.5 seconds) if n % 2 == 1) yield  s"num $n").take(5)
  odds.subscribe(log _, e => log(s"unexpected $e"), () => log("no more odds"))
  val evens = for (n <- Observable.from(0 until 9) if n % 2 == 0) yield s"even number $n"
  evens.subscribe(log _)
  Thread.sleep(5000)
}
