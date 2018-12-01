package org.learningconcurrency
package ch5

import java.util.concurrent.atomic._

object ParUid extends App {
  private val uid = new AtomicLong(0L)
  val seq = 0 until 10000000
  val par = seq.par
  val seqtime = timed {
    for (i <- seq) uid.incrementAndGet()
  }
  log(s"Sequential time $seqtime ms")
  val partime = timed {
    for (i <- par) uid.incrementAndGet()
  }
  log(s"Parallel time $partime ms")
}
