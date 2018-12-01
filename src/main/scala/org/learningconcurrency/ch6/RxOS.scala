package org.learningconcurrency
package ch6

import rx.lang.scala._

object RxOS {
//    val messageBus = Observable.just(TimeModule.systemClock, FileSystemModule.fileModifications).flatten.subscribe(log _)
  val messageBus = Subject[String]()
  val messageLog = subjects.ReplaySubject[String]()
  messageBus.subscribe(log _)
  messageBus.subscribe(messageLog)

}
