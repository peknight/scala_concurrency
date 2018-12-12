package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class CountdownActor extends Actor {
//  var n = 10
//  def receive =
//    if (n > 0) { // 永远都不要这样做
//      case "count" =>
//        log(s"n = $n")
//        n -= 1
//    } else PartialFunction.empty // 并不生效

  val log = Logging(context.system, this)
  var n = 10
  def counting: Actor.Receive = {
    case "count" =>
      n -= 1
      log.info(s"n = $n")
      if (n == 0) context.become(done)
  }

  def done = PartialFunction.empty

  def receive = counting
}
