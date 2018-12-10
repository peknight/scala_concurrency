package org.learningconcurrency
package ch7

import scala.concurrent.stm._

class TSortedList {
  val head = Ref[Node](null)
  override def toString: String = atomic {
    implicit txn =>
      val h = head()
      nodeToString(h)
  }

  import scala.annotation.tailrec
  def insert(x: Int): this.type = atomic {
    implicit txn =>
      @tailrec def insert(n: Node): Unit = {
        if (n.next() == null || n.next().elem > x) {
          n.append(new Node(x, Ref[Node](null)))
        } else insert(n.next())
      }
      if (head() == null || head().elem > x) {
        head() = new Node(x, Ref(head()))
      } else insert(head())
      this
  }
}
