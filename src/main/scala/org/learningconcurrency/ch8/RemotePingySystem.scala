package org.learningconcurrency
package ch8

import akka.actor._

object RemotePingySystem extends App {
  val system = remotingSystem("PingyDimension", 24567)
  val runner = system.actorOf(Props[Runner], "runner")
  runner ! "start"
  Thread.sleep(5000)
  system.terminate()
  Thread.sleep(1000)
}
