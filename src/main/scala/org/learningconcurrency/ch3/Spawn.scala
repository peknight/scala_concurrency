package org.learningconcurrency
package ch3

/**
  * 8. 实现一个转换Scala代码块的方法（spawn），运行一个新的JVM进程并在新进程中运行指定的代码块
  *
  * 做不来做不来。。。
  */
class Spawn {
  def spawn[T](block: => T): T = {
    block
  }
}
