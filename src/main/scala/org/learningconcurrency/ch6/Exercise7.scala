package org.learningconcurrency
package ch6

import rx.lang.scala._

object Exercise7 extends App {
  class RPriorityQueue[T] {
    def add(x: T): Unit = ???
    def pop(): T = ???
    def popped: Observable[T] = ???
  }
}
