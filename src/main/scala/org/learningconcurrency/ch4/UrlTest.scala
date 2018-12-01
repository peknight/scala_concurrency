package org.learningconcurrency
package ch4

import java.util.{Timer, TimerTask}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.io.Source

/**
  * 实现一个命令行程序，在用户输入网站的URL后，能够显示该网站内容。
  * 在用户按Enter键和显示网站内容之前的这段时间中，该程序应该每隔50毫秒就在屏幕上显示一个.，而且这段时间不能超过2秒。
  * 只许使用Future和Promise对象，而且不需使用前面几章介绍的异步基元。
  * 你还可以重用本章介绍的timeout方法。
  */
object UrlTest extends App {
  println("Please enter your url:")
  val url = io.StdIn.readLine

  val timer = new Timer(true)
  val result = Future { Source.fromURL(url).getLines.toList.mkString("\n") } or timeout(1000, "Time out")(timer)
  val loadingTask = new TimerTask { override def run(): Unit = print(".") }
  timer.schedule(loadingTask, 0, 50)

  for (value <- result) {
    loadingTask.cancel
    timer.purge
    println(s"\n$value")
  }

  Thread.sleep(5000)
}
