package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class ParentActor extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case "create" =>
      context.actorOf(Props[ChildActor])
      log.info(s"created a kid; children = ${context.children}")
    case "sayhi" =>
      log.info("Kids, say hi!")
      for (c <- context.children) c ! "sayhi"
    case "stop" =>
      log.info("parent stopping")
      context.stop(self)
  }
}
