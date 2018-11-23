package org.learningconcurrency.ch6

import org.learningconcurrency.learningconcurrency._
import rx.lang.scala._

object ObservablesItems extends App {
  val o = Observable.items("Pascal", "Java", "Scala")
  o.subscribe(name => log(s"learned the $name language"))
  o.subscribe(name => log(s"forgot the $name language"))
}
