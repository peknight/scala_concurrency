package org.learningconcurrency
package ch6

object CompositionErrors extends App {

  import rx.lang.scala.Observable._

  val status = just("ok", "still ok") ++ error(new Exception)
  val fixedStatus = status.onErrorReturn(e => "exception occurred.")
  fixedStatus.subscribe(log _)
  val continuedStatus = status.onErrorResumeNext(e => just("better", "much better"))
  continuedStatus.subscribe(log _)
}
