package org.learningconcurrency
package ch6

import rx.lang.scala._

import scala.concurrent.duration._

object TimeModule {
  import Observable._
  val systemClock = interval(1.seconds).map(t => s"systime: $t")
}
