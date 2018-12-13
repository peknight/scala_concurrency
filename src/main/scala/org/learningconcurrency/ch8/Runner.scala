package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class Runner extends Actor {
  val log = Logging(context.system, this)
  val pingy = context.actorOf(Props[Pingy], "pingy")
  def receive = {
    case "start" =>
      val pongySys = "akka.tcp://PongyDimension@127.0.0.1:24321"
      val pongyPath = "/user/pongy"
      val url = pongySys + pongyPath
      val selection = context.actorSelection(url)
      selection ! Identify(0)
    case ActorIdentity(0, Some(ref)) =>
      pingy ! ref
    case ActorIdentity(0, None) =>
      log.info("Something's wrong - ain't no pongy anywhere!")
      context.stop(self)
    case "pong" =>
      log.info("got a pong from another dimension.")
      context.stop(self)
  }
}
