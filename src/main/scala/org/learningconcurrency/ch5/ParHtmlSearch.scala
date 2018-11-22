package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.collection.GenSeq
import scala.io.Source

/**
  * 这段程序在IDEA中运行表现和在SBT中的表现不太一致，在SBT中运行结果与书中描述相符
  *
  * 使用warmedTimed 使JVM到达稳定状态
  */
object ParHtmlSearch extends App {
  def getHtmlSpec() = Future {
    val url = "http://www.w3.org/MarkUp/html-spec/html-spec.txt"
    val specSrc = Source.fromURL(url)
    try specSrc.getLines.toArray finally specSrc.close
  }

  getHtmlSpec() foreach { case specDoc =>
    def search(d: GenSeq[String]): Double =
      warmedTimed() {
        d.indexWhere(line => line.matches(".*TEXTAREA.*"))
      }
    val seqtime = search(specDoc)
    log(s"Sequential time $seqtime ms")
    val specPar = specDoc.par
    val partime = search(specPar)
    log(s"Parallel time $partime ms")
  }

  Thread.sleep(10000)
}
