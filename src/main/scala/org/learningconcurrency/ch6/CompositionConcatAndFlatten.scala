package org.learningconcurrency
package ch6

import rx.lang.scala.{Observable, Subscription}

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object CompositionConcatAndFlatten extends App {
  import scala.io.Source

  def getQuote(): String = {
    // 书里给的URL不好用，访问不到
    //      val url = "http://www.iheartquotes.com/api/v1/random?" +
    //        "show_permalink=false&show_source=false"
    // 自己找了一个网站配合正则表达式使用风味甚好
    val Quote = """^[^"]*(?:"[^h][^"]*)*"hitokoto":\s*"([^"]*)".*$""".r
    val url = "https://v1.hitokoto.cn/?encode=json"
    val Quote(quote) = blocking {
      Source.fromURL(url).getLines.mkString
    }
    quote
  }

  def fetchQuote(): Future[String] = Future {
    getQuote()
  }

  def fetchQuoteObservable(): Observable[String] = {
    Observable.from(fetchQuote())
  }

  def quotes: Observable[Observable[String]] = {
    for (n <- Observable.interval(0.01 seconds).take(4)) yield {
      for (txt <- fetchQuoteObservable()) yield s"$n) $txt"
    }
  }

  quotes.subscribe(_.subscribe(log _))

  Thread.sleep(5000)

  log(s"Using concat")

  quotes.concat.subscribe(log _)

  Thread.sleep(5000)

  log(s"Using flatten")

  quotes.flatten.subscribe(log _)

  Thread.sleep(5000)

  log(s"Using flatQuotes")

  def flatQuotes: Observable[String]= {
    for {
      n <- Observable.interval(0.01 seconds).take(4)
      txt <- fetchQuoteObservable()
    } yield s"$n) $txt"
  }

  flatQuotes.subscribe(log _)

  Thread.sleep(10000)

}
