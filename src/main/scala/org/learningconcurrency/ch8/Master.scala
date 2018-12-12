package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class Master extends Actor {
  val log = Logging(context.system, this)
  val pingy = ourSystem.actorOf(Props[Pingy], "pingy")
  val pongy = ourSystem.actorOf(Props[Pongy], "pongy")
  def receive = {
    case "start" =>
      pingy ! pongy
    case "pong" =>
      context.stop(self)
  }
  override def postStop() = log.info("master going down")
}
object Master extends App {
  val masta = ourSystem.actorOf(Props[Master], "masta")
  masta ! "start"
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
