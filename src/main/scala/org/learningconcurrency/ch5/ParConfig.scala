package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._

import scala.collection.parallel
import scala.concurrent.forkjoin.ForkJoinPool
import scala.util.Random

object ParConfig extends App {
  val fjpool = new ForkJoinPool(2)
  val customTaskSupport = new parallel.ForkJoinTaskSupport(fjpool)
  val numbers = Random.shuffle(Vector.tabulate(5000000)(i => i))
  val partime = timed {
    val parnumbers = numbers.par
    parnumbers.tasksupport = customTaskSupport
    val n = parnumbers.max
    println(s"largest number $n")
  }
  log(s"Parallel time $partime ms")
}
