package org.learningconcurrency
package ch9

import akka.util._
import akka.actor._
import akka.event._
import akka.pattern._
import org.learningconcurrency.ch9.FTPServerActor.Command

import scala.concurrent._
import ExecutionContext.Implicits.global

class FTPClientActor(implicit val timeout: Timeout) extends Actor {
  import FTPClientActor._
  val log = Logging(context.system, this)
  def unconnected: Actor.Receive = {
    case Start(host) =>
      val serverActorPath = s"akka.tcp://FTPServerSystem@$host/user/server"
      val serverActorSel = context.actorSelection(serverActorPath)
      serverActorSel ! Identify(())
      context.become(connection(sender))
  }

  def connection(clientApp: ActorRef): Actor.Receive = {
    case ActorIdentity(_, Some(ref)) =>
      clientApp ! true
      log.info("found: " + ref)
      context.become(connected(ref))
    case ActorIdentity(_, None) =>
      clientApp ! false
      context.become(unconnected)
  }

  def connected(serverActor: ActorRef): Actor.Receive = {
    case command: Command =>
      (serverActor ? command) pipeTo sender
  }

  def receive = unconnected
}
object FTPClientActor {
  case class Start(host: String)
}
