package org.learningconcurrency
package ch8

import akka.actor._

object ActorsCreate extends App {
  val hiActor: ActorRef =
    ourSystem.actorOf(HelloActor.props("hi"), name = "greeter")
  hiActor ! "hi"
  Thread.sleep(1000)
  hiActor ! "hola"
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
