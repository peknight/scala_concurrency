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
}
