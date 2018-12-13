package org.learningconcurrency
package ch8

import akka.actor._

class GracefulPingy extends Actor {
  val pongy = context.actorOf(Props[Pongy], "pongy")
  context.watch(pongy)
  def receive = {
    case "Die, Pingy!" =>
      context.stop(pongy)
    case Terminated(`pongy`) =>
      context.stop(self)
  }
}
