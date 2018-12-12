package org.learningconcurrency
package ch8

import akka.actor._

object ActorsUnhandled extends App {
  val deafActor: ActorRef = ourSystem.actorOf(Props[DeafActor], name = "deafy")
  deafActor ! "hi"
  Thread.sleep(1000)
  deafActor ! 1234
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
