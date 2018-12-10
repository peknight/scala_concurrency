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
  @tailrec
  final def insert(n: Node, x: Int)(implicit txn: InTxn): Unit = {
    if (n.next() == null || n.next().elem > x) {
      n.append(new Node(x, Ref[Node](null)))
    } else insert(n.next(), x)
  }

  def insert(x: Int): this.type = atomic {
    implicit txn =>
//      @tailrec def insert(n: Node): Unit = {
//        if (n.next() == null || n.next().elem > x) {
//          n.append(new Node(x, Ref[Node](null)))
//        } else insert(n.next())
//      }

      if (head() == null || head().elem > x) {
        head() = new Node(x, Ref(head()))
      } else insert(head(), x)
      this
  }

  def pop(xs: TSortedList, n: Int): Unit = atomic {
    implicit txn =>
      var left = n
      while (left > 0) {
        xs.head() = xs.head().next()
        left -= 1
      }
  }
}
object TSortedList extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  val sortedList = new TSortedList
  val f = Future { sortedList.insert(1); sortedList.insert(4) }
  val g = Future { sortedList.insert(2); sortedList.insert(3) }
  for (_ <- f; _ <- g) log(s"sorted list - $sortedList")
  Thread.sleep(1000)
}
