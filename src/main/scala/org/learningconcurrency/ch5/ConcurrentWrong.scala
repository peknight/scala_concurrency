package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._

import scala.collection._
import scala.concurrent._
import ExecutionContext.Implicits.global

object ConcurrentWrong extends App {
  import ParHtmlSearch.getHtmlSpec
  import org.learningconcurrency.ch4.FuturesCallbacks.getUrlSpec

  import java.util.concurrent.ConcurrentSkipListSet
  import scala.collection.convert.decorateAsScala._

  def intersection(a: GenSet[String], b: GenSet[String]) = {
//    val result = new mutable.HashSet[String]
//    for (x <- a.par) if (b contains x) result.add(x)
//    result
    val skiplist = new ConcurrentSkipListSet[String]
    for (x <- a.par) if (b contains x) skiplist.add(x)
    val result: Set[String] = skiplist.asScala
    result
  }

  val ifut = for {
    htmlSpec <- getHtmlSpec
    urlSpec <- getUrlSpec
  } yield {
    val htmlWords = htmlSpec.mkString.split("\\s+").toSet
    val urlWords = urlSpec.mkString.split("\\s+").toSet
    intersection(htmlWords, urlWords)
  }

  ifut onComplete {
    case t => log(s"Result - $t")
  }
  Thread.sleep(3000)
}
