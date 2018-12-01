package org.learningconcurrency
package ch5

object CustomCharCount extends App {
  val txt = "A custom text " * 250000
  val partxt = new ParString(txt)
  val seqtime = warmedTimed(50) {
    txt.foldLeft(0) {(n, c) =>
      if (Character.isUpperCase(c)) n + 1 else n
    }
  }
  log(s"Sequential time - $seqtime ms")
  val partime = warmedTimed(50) {
    partxt.aggregate(0) (
      (n, c) => if (Character.isUpperCase(c)) n + 1 else n,
      _ + _
    )
  }
  log(s"Parallel time - $partime ms")
}
