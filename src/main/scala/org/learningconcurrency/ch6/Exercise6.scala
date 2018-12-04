package org.learningconcurrency
package ch6

import rx.lang.scala._

import scala.collection._

object Exercise6 extends App {
  class RMap[K, V] {
    private[this] val map: mutable.Map[K, Subject[V]] = mutable.Map.empty

    def update(k: K, v: V): Unit = map.get(k) match {
      case Some(o) => o.onNext(v)
//      case None => {
//        val subject = Subject[V]()
//        map(k) = subject
//        subject.onNext(v)
//      }
      case _ =>
    }

    def apply(k: K): Observable[V] = map.get(k) match {
      case Some(o) => o;
      case None => {
        val subject = Subject[V]()
        map(k) = subject
        subject
      }
    }
  }
}
