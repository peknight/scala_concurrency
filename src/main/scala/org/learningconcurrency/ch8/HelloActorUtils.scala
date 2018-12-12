package org.learningconcurrency
package ch8

import akka.actor._

class HelloActorUtils {
  val defaultHi = "Aloha!"
  def defaultProps() = Props(new HelloActor(defaultHi))
}
