package org.learningconcurrency

import scala.concurrent.stm._

package object ch7 {
  def nodeToString(n: Node): String = atomic {
    implicit txn =>
      val b = new StringBuilder
      var curr = n
      while (curr != null) {
        b ++= s"${curr.elem}, "
        curr = curr.next()
      }
      b.toString
  }

  def pop(xs: TSortedList, n: Int): Unit = atomic {
    implicit txn =>
      var left = n
      while (left > 0) {
        xs.head() = xs.head().next()
        left -= 1
      }
  }

  def headWait(lst: TSortedList): Int = atomic {
    implicit txn =>
//      while (lst.head() == null) {} // 永远都不应这样做
//      lst.head().elem
      if (lst.head() != null) lst.head().elem
      else retry
  }

  val myLog = TxnLocal("", afterCommit = { log _ })
  def clearList(lst: TSortedList): Unit = atomic {
    implicit txn =>
      while (lst.head() != null) {
        TxnLocal
        myLog() = myLog() + "\nremoved " + lst.head().elem
        lst.head() = lst.head().next()
      }
  }

}
