package org.learningconcurrency
package ch8

import akka.actor._

class Router extends Actor {
  var i = 0
  val children = for (_ <- 0 until 4) yield context.actorOf(Props[StringPrinter])
  def receive = {
    case msg => children(i) forward msg; i = (i + 1) % 4
  }
}
object Router extends App {
  val router = ourSystem.actorOf(Props[Router], "router")
  router ! "Hola"
  router ! "Hey!"
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
