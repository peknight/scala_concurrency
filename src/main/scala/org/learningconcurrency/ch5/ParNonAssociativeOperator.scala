package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._
import scala.collection._
import scala.concurrent._
import ExecutionContext.Implicits.global

object ParNonAssociativeOperator extends App {
  def test(doc: GenIterable[Int]): Unit = {
    val seqtext = doc.seq.reduceLeft(_ - _)
    val partext = doc.par.reduce(_ - _)
    log(s"Sequential result - $seqtext\n")
    log(s"Parallel result - $partext\n")
  }

  test(0 until 30)
}
