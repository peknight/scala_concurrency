package org.learningconcurrency
package ch8

import java.util.{Timer, TimerTask}

import akka.actor._
import akka.pattern._

import scala.concurrent._
import ExecutionContext.Implicits.global

object Exercise1 extends App {
  class TimerActor extends Actor {
    private[this] val timer = new Timer(true)
    def receive = {
      case Register(t) => Future {
        val promise = Promise[Unit]()
        timer.schedule(new TimerTask {
          override def run(): Unit = promise.success()
        }, t)
        (for (_ <- promise.future) yield Timeout) pipeTo sender
      }
    }
  }
  case class Register(t: Long)
  case object Timeout
}

