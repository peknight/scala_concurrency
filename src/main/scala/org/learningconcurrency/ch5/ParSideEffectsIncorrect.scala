package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.collection.GenSet

object ParSideEffectsIncorrect extends App {
  def intersectionSize(a: GenSet[Int], b: GenSet[Int]): Int = {
    var total = 0
    for (x <- a) if (b contains x) total += 1
    total
  }

  val a = (0 until 100000).toSet
  val b = (0 until 100000 by 4).toSet
  val seqres = intersectionSize(a, b)
  val parres = intersectionSize(a.par, b.par)
  log(s"Sequential result - $seqres")
  log(s"Parallel result - $parres")
}
