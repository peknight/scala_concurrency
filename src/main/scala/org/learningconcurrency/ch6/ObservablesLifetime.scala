package org.learningconcurrency
package ch6

import rx.lang.scala.Observable

object ObservablesLifetime extends App {
  val classics = List("Good, bad, ugly", "Titanic", "Die Hard")
  val movies = Observable.from(classics)

  /* 下面注释这一段会抛NoSuchMethodException，原因不明。因此使用后面的重载替换 */
//  movies.subscribe(new Observer[String] {
//    override def onNext(value: String): Unit = log(s"Movies Watchlist - $value")
//
//    override def onError(error: Throwable): Unit = log(s"Ooops - $error")
//
//    override def onCompleted(): Unit = log(s"No more movies.")
//  })

  movies.subscribe(
    value => log(s"Movies Watchlist - $value"),
    error => log(s"Ooops - $error"),
    () => log(s"No more movies.")
  )
}

