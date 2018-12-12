package org.learningconcurrency
package ch8

import akka.actor._
import akka.event.Logging

class HelloActor(val hello: String) extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case `hello` => log.info(s"Received a '$hello' ... $hello!")
    case msg =>
      log.info(s"Unexpected message '$msg'")
      context.stop(self)
  }
}
object HelloActor {
  def props(hello: String) = Props(new HelloActor(hello))
  def propsAlt(hello: String) = Props(classOf[HelloActor], hello)
}
