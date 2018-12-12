package org.learningconcurrency
package ch7

import scala.concurrent.stm._

object Exercise1 extends App {
  class TPair[P, Q](pinit: P, qinit: Q) {
    private[this] val p: Ref[P] = Ref[P](pinit)
    private[this] val q: Ref[Q] = Ref[Q](qinit)
    def first(implicit txn: InTxn): P = p.single()
    def first_=(x: P)(implicit txn: InTxn): P = {
      p.single() = x
      x
    }
    def second(implicit txn: InTxn): Q = q.single()
    def second_(x: Q)(implicit txn: InTxn): Q = {
      q.single() = x
      x
    }
    // =:= 强制约束类型P与Q相等 类似如果有 P <:< Q则约束P为Q的子类型 P <%< Q表示P可以通过隐式转换变为Q
    def swap()(implicit e: P =:= Q, txn: InTxn): Unit = {
      atomic {
        implicit txn =>
          val oldP = p()
          p() = e(q())
          q() = e(oldP)
      }
    }
  }
}
