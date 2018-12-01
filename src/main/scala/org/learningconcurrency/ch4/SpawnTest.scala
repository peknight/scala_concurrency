package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.sys.process._

object SpawnTest extends App {
  def spawn(command: String): Future[Int] = {
    Future {
      command.!
    }
  }
  println(Await.result(spawn("ls /"), Duration.Inf))
}
