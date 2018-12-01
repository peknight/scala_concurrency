package org.learningconcurrency
package ch3

import scala.collection._

object CollectionsBad extends App {

  val buffer = mutable.ArrayBuffer[Int]()
  def asyncAdd(numbers: Seq[Int]) = execute {
    buffer.synchronized {
      buffer ++= numbers
      log(s"buffer = $buffer")
    }
  }

  asyncAdd(0 until 10)
  asyncAdd(10 until 20)
  Thread.sleep(500)
}