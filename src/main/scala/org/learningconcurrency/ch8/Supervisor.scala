package org.learningconcurrency
package ch8

import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor._

class Supervisor extends Actor {
  val child = context.actorOf(Props[Naughty], "naughty")
  def receive = PartialFunction.empty
  override val supervisorStrategy =
    OneForOneStrategy() {
      case ake: ActorKilledException => Restart
      case _ => Escalate
    }
}
object Supervisor extends App {
  ourSystem.actorOf(Props[Supervisor], "super")
  ourSystem.actorSelection("/user/super/*") ! Kill
  ourSystem.actorSelection("/user/super/*") ! "sorry about that"
  ourSystem.actorSelection("/user/super/*") ! "kaboom".toList
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
