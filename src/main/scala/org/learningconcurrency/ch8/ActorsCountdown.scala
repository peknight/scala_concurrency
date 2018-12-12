package org.learningconcurrency
package ch8

import akka.actor.Props

object ActorsCountdown extends App {
  val countdown = ourSystem.actorOf(Props[CountdownActor])
  for (i <- 0 until 20) countdown ! "count"
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
