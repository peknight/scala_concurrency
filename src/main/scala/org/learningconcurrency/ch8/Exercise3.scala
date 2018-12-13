package org.learningconcurrency
package ch8

import akka.actor._

object Exercise3 extends App {
  class SessionActor(password: String, r: ActorRef) extends Actor {
    private def checkPassword: PartialFunction[Any, Unit] = {
      case StartSession(p) if password == p => context.become(dispatch)
    }
    private def dispatch:PartialFunction[Any, Unit] = {
      case EndSession => context.become(checkPassword)
      case msg => r ! msg
    }

    def receive = checkPassword
  }

  case class StartSession(password: String)
  case object EndSession

}
