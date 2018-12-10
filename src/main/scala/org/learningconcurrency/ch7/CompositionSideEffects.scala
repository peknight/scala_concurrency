package org.learningconcurrency
package ch7

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.stm._

object CompositionSideEffects extends App {
  val myValue = Ref(0)
  def inc() = atomic {
    implicit txn =>
      val valueAtStart = myValue()
      Txn.afterCommit {
        _ =>
          log(s"Incrementing $valueAtStart")
      }
      Txn.afterRollback {
        _ =>
          log(s"rollin' back")
      }
      myValue() = myValue() + 1
  }
  Future { inc() }
  Future { inc() }
  Thread.sleep(5000)
}
