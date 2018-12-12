package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent._
import ExecutionContext.Implicits.global

class Pingy extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case pongyRef: ActorRef =>
      implicit val timeout = Timeout(2 seconds)
      val f = pongyRef ? "ping"
      f pipeTo sender
  }
}
