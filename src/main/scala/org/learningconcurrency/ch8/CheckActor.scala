package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class CheckActor extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case path: String =>
      log.info(s"checking path $path")
      context.actorSelection(path) ! Identify(path)
    case ActorIdentity(path, Some(ref)) =>
      log.info(s"found actor $ref at $path")
    case ActorIdentity(path, None) =>
      log.info(s"could not find an actor at $path")
  }
}
object CheckActor {
  def main(args: Array[String]): Unit = {
    val checker = ourSystem.actorOf(Props[CheckActor], "checker")
    checker ! "../*"
    checker ! "../../*"
    checker ! "/system/*"
    checker ! "/user/checker2"
    Thread.sleep(1000)
  }
}
