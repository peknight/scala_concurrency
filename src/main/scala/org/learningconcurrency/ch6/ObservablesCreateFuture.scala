package org.learningconcurrency
package ch6

import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object ObservablesCreateFuture extends App {
  val f = Future {"Back to the Future(s)"}
//  val o = Observable.create[String] {
//    obs =>
//      for (s <- f) {
//        obs.onNext(s);
//        obs.onCompleted();
//      }
//      for (t <- f.failed) {
//        obs.onError(t)
//      }
//      Subscription()
//  }
  val o = Observable.from(Future {"Back to the Future(s)"})
  o.subscribe(log _)
}
