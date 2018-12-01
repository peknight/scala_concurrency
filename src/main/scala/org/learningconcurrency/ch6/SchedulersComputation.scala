package org.learningconcurrency
package ch6

import rx.lang.scala._

object SchedulersComputation extends App {
  val scheduler = schedulers.ComputationScheduler()
  val numbers = Observable.from(0 until 20)
  numbers.subscribe(n => log(s"num $n"))
  numbers.observeOn(scheduler).subscribe(n => log(s"num $n"))
  Thread.sleep(2000)

}
