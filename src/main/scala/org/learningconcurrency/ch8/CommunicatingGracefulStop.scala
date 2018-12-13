package org.learningconcurrency
package ch8

import akka.actor._
import akka.pattern._

import scala.util._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

object CommunicatingGracefulStop extends App {
  val grace = ourSystem.actorOf(Props[GracefulPingy], "grace")
  val stopped = gracefulStop(grace, 3.seconds, "Die, Pingy!")
  stopped onComplete {
    case Success(x) =>
      log("graceful shutdown successful")
      ourSystem.terminate()
    case Failure(t) =>
      log("grace not stopped")
      ourSystem.terminate()
  }
  Thread.sleep(1000)
}
