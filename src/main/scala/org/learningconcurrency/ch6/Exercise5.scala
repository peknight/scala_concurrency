package org.learningconcurrency
package ch6

import Exercise4._

object Exercise5 extends App {
  class RCell[T] extends Signal[T] {
    def :=(x: T): Unit = ???
  }
}
