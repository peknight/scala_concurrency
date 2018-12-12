package org.learningconcurrency
package ch7

import scala.concurrent.stm._

object Exercise2 extends App {
  class MVar[T] {
    private[this] val mVar = Ref[Option[T]](None)
    def put(x: T)(implicit txn: InTxn): Unit = atomic {
      implicit txn =>
        mVar() match {
          case Some(_) => retry
          case None => mVar() = Some(x)
        }
    }
    def take()(implicit txn: InTxn): T = atomic {
      implicit txn =>
        mVar() match {
          case Some(x) => {
            mVar() = None
            x
          }
          case None => retry
        }
    }
  }
  object MVar {
    def swap[T](a: MVar[T], b: MVar[T])(implicit txn: InTxn) = atomic {
      implicit txn =>
        val oldA = a.take()
        a.put(b.take())
        b.put(oldA)
    }
  }
}
