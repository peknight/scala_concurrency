package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class StringPrinter extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case msg => log.info(s"printer got message '$msg'")
  }
  override def preStart(): Unit = log.info(s"printer preStart.")
  override def postStop(): Unit = log.info(s"printer postStop.")
}
