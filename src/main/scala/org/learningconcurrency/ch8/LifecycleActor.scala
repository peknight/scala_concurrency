package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

class LifecycleActor extends Actor {
  val log = Logging(context.system, this)
  var child: ActorRef = _
  def receive = {
    case num: Double => log.info(s"got a double - $num")
    case num: Int => log.info(s"got an integer - $num")
    case lst: List[_] => log.info(s"list - ${lst.head}, ...")
    case txt: String => child ! txt
  }
  override def preStart(): Unit = {
    log.info("about to start")
    child = context.actorOf(Props[StringPrinter], "kiddo")
  }

  override def preRestart(t: Throwable, msg: Option[Any]): Unit = {
    log.info(s"about to restart because of $t, during message $msg")
    super.preRestart(t, msg)
  }

  override def postRestart(t: Throwable): Unit = {
    log.info(s"just restarted due to $t")
    super.postRestart(t)
  }

  override def postStop() = log.info("just stopped")
}
object LifecycleActor extends App {
  val testy = ourSystem.actorOf(Props[LifecycleActor], "testy")
  testy ! math.Pi
  testy ! "hi there!"
  testy ! Nil
  Thread.sleep(1000)
  ourSystem.terminate()
  Thread.sleep(1000)
}
