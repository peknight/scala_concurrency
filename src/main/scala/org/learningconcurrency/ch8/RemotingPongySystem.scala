package org.learningconcurrency
package ch8

import akka.actor._

object RemotingPongySystem extends App {
  val system = remotingSystem("PongyDimension", 24321)
  val pongy = system.actorOf(Props[Pongy], "pongy")
  Thread.sleep(15000)
  system.terminate()
}
