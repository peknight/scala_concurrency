package org.learningconcurrency
package ch1

class Printer(val greeting: String) {
  def printMessage(): Unit = println(greeting + "!")
  def printNumber(x: Int): Unit = {
    println("Number " + x)
  }
}
