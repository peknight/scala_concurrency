package org.learningconcurrency
package ch5

import scala.collection._

object ParNonCommutativeOperator extends App {
  val doc = mutable.ArrayBuffer.tabulate(20)(i => s"Page $i")
  def test(doc: GenIterable[String]): Unit = {
    val seqtext = doc.seq.reduceLeft(_ + _)
    val partext = doc.par.reduce(_ + _)
    log(s"Sequential result - $seqtext\n")
    log(s"Parallel result - $partext\n")
  }
  test(doc)
  test(doc.toSet)
}
