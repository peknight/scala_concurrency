package org.learningconcurrency
package ch8

import akka.actor._
import akka.event._

import scala.collection._
import scala.io.Source

class DictionaryActor extends Actor {
  private val log = Logging(context.system, this)
  private val dictionary = mutable.Set[String]()

  def receive = uninitialized

  def uninitialized: PartialFunction[Any, Unit] = {
    case DictionaryActor.Init(path) =>
      val stream = getClass.getResourceAsStream(path)
      val words = Source.fromInputStream(stream)
      for (w <- words.getLines) dictionary += w
      context.become(initialized)
  }

  def initialized: PartialFunction[Any, Unit] = {
    case DictionaryActor.IsWord(w) =>
      log.info(s"word '$w' exists: ${dictionary(w)}")
    case DictionaryActor.End =>
      dictionary.clear()
      context.become(uninitialized)
  }

  override def unhandled(message: Any) = {
    log.info(s"cannot handle message $message in this state.")
  }

}
object DictionaryActor {
  case class Init(path: String)
  case class IsWord(w: String)
  case object End

  def main(args: Array[String]): Unit = {
    val dict = ourSystem.actorOf(Props[DictionaryActor], "dictionary")
    dict ! DictionaryActor.IsWord("program")
    Thread.sleep(1000)
    dict ! DictionaryActor.Init("/org/learningconcurrency/words.txt")
    Thread.sleep(1000)
    dict ! DictionaryActor.IsWord("program")
    Thread.sleep(1000)
    dict ! DictionaryActor.IsWord("balaban")
    Thread.sleep(1000)
    dict ! DictionaryActor.End
    Thread.sleep(1000)
    ourSystem.terminate()
    Thread.sleep(1000)
  }
}
