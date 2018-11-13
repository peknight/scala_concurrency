package org.learningconcurrency.ch3

import org.learningconcurrency.learningconcurrency._

import scala.sys.process._

object ProcessRun extends App {
  def lineCount(filename: String): Int = {
    val output = s"wc $filename".!!
    output.trim.split(" ").head.toInt
  }

  val command = "ls"
  val exitcode = command.!
  log(s"command exited with status $exitcode")
  log(lineCount("build.sbt"))
}
