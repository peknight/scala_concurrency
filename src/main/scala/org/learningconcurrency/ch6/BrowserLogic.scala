package org.learningconcurrency
package ch6

import rx.lang.scala._

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global
import scala.io.Source

trait BrowserLogic {
  self: BrowserFrame =>

  def suggestRequest(term: String): Observable[String] = {
    val url = "http://suggestqueries.google.com/" +
      s"complete/search?client=firefox&q=$term"
    val request = Future { Source.fromURL(url).mkString }
    Observable.from(request).timeout(0.5.seconds).onErrorReturn(e => "(no suggestion)")
  }

  def pageRequest(url: String): Observable[String] = {
    val request = Future { Source.fromURL(url).mkString }
    Observable.from(request).timeout(10.seconds).onErrorReturn(e => s"Could not load page: $e")
  }
  urlfield.texts.map(suggestRequest).concat
    .observeOn(swingScheduler)
    .subscribe(response => pagefield.text = response)
  button.clicks.map(_ => pageRequest(urlfield.text)).concat
    .observeOn(swingScheduler)
    .subscribe(response => pagefield.text = response)
}
