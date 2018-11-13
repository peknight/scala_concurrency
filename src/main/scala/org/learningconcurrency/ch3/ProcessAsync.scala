package org.learningconcurrency.ch3

import org.learningconcurrency.learningconcurrency._

import scala.sys.process._

object ProcessAsync extends App {
  val lsProcess = "ls -R /".run()
  Thread.sleep(1000)
  log("Timeout - killing ls!")
  lsProcess.destroy()
}
