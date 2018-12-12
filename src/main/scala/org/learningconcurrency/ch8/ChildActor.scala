package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class ChildActor extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case "sayhi" =>
      val parent = context.parent
      log.info(s"my parent $parent made me say hi!")
  }
  override def postStop() = {
    log.info("child stopped!")
  }
}
