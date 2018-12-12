package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class DeafActor extends Actor {
  val log = Logging(context.system, this)
  def receive = PartialFunction.empty

  override def unhandled(msg: Any) = msg match {
    case msg: String => log.info(s"I do not hear '$msg'")
    case msg => super.unhandled(msg)
  }
}
