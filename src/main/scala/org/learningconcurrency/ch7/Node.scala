package org.learningconcurrency
package ch7

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.stm._


case class Node(elem: Int, next: Ref[Node]) {
  def append(n: Node): Unit = atomic {
    implicit txn =>
      val oldNext = next()
      next() = n
      n.next() = oldNext
  }
  def nextNode: Node = next.single()

  def appendIfEnd(n: Node) = next.single.transform {
    oldNext => if (oldNext == null) n else oldNext
  }

}
object Node extends App {
  val nodes = Node(1, Ref(Node(4, Ref(Node(5, Ref[Node](null))))))
  val f = Future { nodes.append(Node(2, Ref[Node](null))) }
  val g = Future { nodes.append(Node(3, Ref[Node](null))) }

  for (_ <- f; _ <- g) log(s"Next node is: ${nodes.nextNode}")
  Thread.sleep(1000)
}

