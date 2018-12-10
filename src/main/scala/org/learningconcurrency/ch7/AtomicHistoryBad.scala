package org.learningconcurrency
package ch7

import scala.concurrent._
import ExecutionContext.Implicits.global

object AtomicHistoryBad extends App {
  import java.util.concurrent.atomic._
  val urls = new AtomicReference[List[String]](Nil)
  val clen = new AtomicInteger(0)

  import scala.annotation.tailrec
  def addUrl(url: String): Unit = {
    @tailrec def append(): Unit = {
      val oldUrls = urls.get
      val newUrls = url :: oldUrls
      if (!urls.compareAndSet(oldUrls, newUrls)) append
    }
    append
    clen.addAndGet(url.length + 1)
  }

  def getUrlArray(): Array[Char] = {
    val array = new Array[Char](clen.get)
    val urlList = urls.get
    for ((ch, i) <- urlList.map(_ + "\n").flatten.zipWithIndex) {
      array(i) = ch
    }
    array
  }

  Future {
    try {
      log(s"sending: ${getUrlArray.mkString}")
    } catch {
      case e: Exception => log(s"Houston... $e")
    }
  }
  Future {
    for (_ <- 1 to 10000) {
      addUrl("http://scala-lang.org")
      addUrl("https://github.com/scala/scala")
      addUrl("http://www.scala-lang.org/api")
    }
    log("done browsing")
  }
  Thread.sleep(1000)
}
