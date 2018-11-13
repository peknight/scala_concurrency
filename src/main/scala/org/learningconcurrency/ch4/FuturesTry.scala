package org.learningconcurrency.ch4

import org.learningconcurrency.learningconcurrency._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}

object FuturesTry extends App {
  def handleMessage(t: Try[String]) = t match {
    case Success(msg) => log(msg)
    case Failure(error) => log(s"unexpected failure - $error")
  }

  val threadName: Try[String] = Try(Thread.currentThread.getName)
  val someText: Try[String] = Try("Try objects are synchronous")
  val message: Try[String] = for {
    tn <- threadName
    st <- someText
  } yield s"Message $st was created on t = $tn"
  handleMessage(message)
}
