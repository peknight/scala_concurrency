package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._
import java.util.concurrent.atomic._
import scala.collection._

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
