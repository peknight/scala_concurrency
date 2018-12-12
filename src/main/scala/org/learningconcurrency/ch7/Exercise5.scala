package org.learningconcurrency
package ch7

import scala.collection.immutable.Queue
import scala.concurrent.stm._

object Exercise5 extends App {
  class TQueue[T] {
    private[this] val queue = Ref[Queue[T]](Queue.empty[T])
    def enqueue(x: T)(implicit txn: InTxn): Unit = queue.single.transform(list => list :+ x)
    def dequeue()(implicit txn: InTxn): T = atomic {
      implicit txn =>
        queue().dequeueOption match {
          case None => retry
          case Some((x, q)) =>
            queue() = q
            x
        }
    }
  }
}
