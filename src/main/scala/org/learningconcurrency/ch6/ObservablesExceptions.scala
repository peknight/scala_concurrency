package org.learningconcurrency
package ch6

import rx.lang.scala.Observable

object ObservablesExceptions extends App {
  val exc = new RuntimeException
  val o = Observable.items(1, 2) ++ Observable.error(exc) ++ Observable.items(3, 4)
  o.subscribe(
    x => log(s"number $x"),
    t => log(s"an error occurred: $t")
  )
}
