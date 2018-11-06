package org.learningconcurrency

package object learningconcurrency {
  def log(msg: Any): Unit = println(s"${Thread.currentThread.getName}: ${msg.toString}")
}
