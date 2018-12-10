package org.learningconcurrency
package ch7

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.concurrent.stm._

object AtomicHistorySTM extends App {
  val urls = Ref[List[String]](Nil)
  val clen = Ref(0)

  def addUrl(url: String): Unit = atomic {
    implicit txn =>
      urls() = url :: urls()
      clen() = clen() + url.length + 1
  }

  def getUrlArray(): Array[Char] = atomic {
    implicit txn =>
      val array = new Array[Char](clen())
      for ((ch, i) <- urls().map(_ + "\n").flatten.zipWithIndex) {
        array(i) = ch
      }
      array
  }

  Future {
    for (_ <- 1 to 10000) {
      addUrl("http://scala-lang.org")
      addUrl("https://github.com/scala/scala")
      addUrl("http://www.scala-lang.org/api")
    }
    log("done browsing")
  }
  Thread.sleep(25)
  Future {
    try {
      log(s"sending: ${getUrlArray.mkString}")
    } catch {
      case e: Exception => log(s"Ayayay... $e")
    }
  }
  Thread.sleep(5000)
}
