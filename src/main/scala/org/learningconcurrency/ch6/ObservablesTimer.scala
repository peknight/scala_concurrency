package org.learningconcurrency.ch6

import org.learningconcurrency.learningconcurrency._
import rx.lang.scala._
import scala.concurrent.duration._

object ObservablesTimer extends App {
  val o = Observable.timer(1.seconds)
  o.subscribe(_ => log("Timeout!"))
  o.subscribe(_ => log("Another Timeout!"))
  Thread.sleep(2000)
}
