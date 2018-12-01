package org.learningconcurrency
package ch6

import rx.lang.scala.{Observable, Subscription}

object ObservablesCreate extends App {
  val vms = Observable.create[String] {
    obs =>
      obs.onNext("JVM")
      obs.onNext("DartVM")
      obs.onNext("V8")
      obs.onCompleted()
      Subscription()
  }
  vms.subscribe(log _, e => log(s"oops - $e"), () => log("Done!"))

}
