package org.learningconcurrency
package ch8

import akka.actor._

object ActorsHierarchy extends App {
  val parent = ourSystem.actorOf(Props[ParentActor], "parent")
  parent ! "create"
  parent ! "create"
  Thread.sleep(1000)
  parent ! "sayhi"
  Thread.sleep(1000)
  parent ! "stop"
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
