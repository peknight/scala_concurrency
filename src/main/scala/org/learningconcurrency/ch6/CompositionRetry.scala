package org.learningconcurrency
package ch6

import rx.lang.scala.{Observable, Subscription}

object CompositionRetry extends App {

  def randomQuote = Observable.create[String] {
    obs =>
      obs.onNext(CompositionConcatAndFlatten.getQuote())
      obs.onCompleted()
      Subscription()
  }
  log(s"randomQuote 1")
  randomQuote.subscribe(log _)
  log(s"randomQuote 2")
  randomQuote.subscribe(log _)
  log(s"randomQuote 3")
  randomQuote.subscribe(log _)


  log(s"retry")

  import Observable._
  def errorMessage = just("Retrying...") ++ error(new Exception)
  def quoteMessage = for {
    text <- randomQuote
    message <- if (text.size < 20) just(text) else errorMessage
  } yield message
  quoteMessage.retry(5).subscribe(log _)
}
