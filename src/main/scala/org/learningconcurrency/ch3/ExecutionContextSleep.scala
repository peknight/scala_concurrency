package org.learningconcurrency.ch3

import org.learningconcurrency.learningconcurrency._

object ExecutionContextSleep extends App {
  for (i <- 0 until 32) execute {
    Thread.sleep(2000)
    log(s"Task $i completed.")
  }
  Thread.sleep(10000)
}
