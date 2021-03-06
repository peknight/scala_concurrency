package org.learningconcurrency
package ch5

import scala.util.Random

object ParBasic extends App {
  val numbers = Random.shuffle(Vector.tabulate(5000000)(i => i))
  val seqtime = timed { numbers.max }
  log(s"Sequential time $seqtime ms")
  val parNumbers = numbers.par
  val partime = timed { parNumbers.max }
  log(s"Parallel time $partime ms")
}
