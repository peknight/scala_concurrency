package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}

/**
  *
  */
object ComposePromiseTest extends App {
  implicit class ComposePromise[T](promise: Promise[T]) {
    def compose[S](f: S => T): Promise[S] = {
      val ps = Promise[S]
      ps.future.onComplete {
        case Success(s) => Future(promise.trySuccess(f(s)))
        case Failure(e) => promise.tryFailure(e)
      }
      ps
    }
  }
}
