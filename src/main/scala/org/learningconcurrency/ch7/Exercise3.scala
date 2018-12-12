package org.learningconcurrency
package ch7

import scala.concurrent.stm._

object Exercise3 extends App {
  def atomicRollbackCount[T](block: InTxn => T): (T, Int) = {
    var count = 0;
    atomic {
      implicit txn =>
        Txn.afterRollback {
          _ =>
            count += 1
        }
        (block(txn), count)
    }
  }
}
