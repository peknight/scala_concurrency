package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._
//import scala.collection.par._
//import scala.collection.par.Scheduler.Implicits.global

/**
  * 看起来ScalaBlitz没有使用scala2.12编译的版本
  */
object BlitzComparison extends App {
  val array = (0 until 100000).toArray
  val seqtime = warmedTimed(1000) {
    array.reduce(_ + _)
  }
  val partime = warmedTimed(1000) {
    array.par.reduce(_ + _)
  }
//  val blitztime = warmedTimed(1000) {
//    array.toPar.reduce(_ + _)
//  }

  log(s"sequential time - $seqtime")
  log(s"parallel time - $partime")
//  log(s"ScalaBlitz time - $blitztime")
}
