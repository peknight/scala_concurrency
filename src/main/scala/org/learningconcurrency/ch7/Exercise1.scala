package org.learningconcurrency
package ch7

import scala.concurrent.stm._

object Exercise1 extends App {
  class TPair[P, Q](pinit: P, qinit: Q) {
    def first(implicit txn: InTxn): P = ???
    def first_=(x: P)(implicit txn: InTxn): P = ???
    def second(implicit txn: InTxn): Q = ???
    def second_(x: Q)(implicit txn: InTxn): Q = ???
    def swap()(implicit e: P =:= Q, txn: InTxn): Unit = ???
  }
}
