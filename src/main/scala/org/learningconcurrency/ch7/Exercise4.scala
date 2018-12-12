package org.learningconcurrency
package ch7

import scala.concurrent.stm._

object Exercise4 extends App {
  case class ReachRetryMaxCountException(val count: Int) extends Exception

  def atomicWithRetryMax[T](n: Int)(block: InTxn => T): T = {
    var retry = 0
    atomic {
      implicit txn =>
        Txn.afterRollback (_ => retry += 1)
        if (retry >= n) {
          throw ReachRetryMaxCountException(retry)
        }
        block(txn)
    }
  }
}
