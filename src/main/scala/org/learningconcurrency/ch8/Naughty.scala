package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class Naughty extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case s: String => log.info(s)
    case msg => throw new RuntimeException
  }
  override def postRestart(t: Throwable) =
    log.info("naughty restarted")
}
