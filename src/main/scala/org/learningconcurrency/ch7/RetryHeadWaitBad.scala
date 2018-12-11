package org.learningconcurrency
package ch7

import scala.concurrent._
import ExecutionContext.Implicits.global

object RetryHeadWaitBad extends App {
  val myList = new TSortedList
  Future {
    blocking {
      log(s"The first element is ${headWait(myList)}")
    }
  }
  Thread.sleep(1000)

  Future { myList.insert(1) }

  Thread.sleep(1000)
}
