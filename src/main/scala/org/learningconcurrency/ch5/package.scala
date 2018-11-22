package org.learningconcurrency

package object ch5 {
  @volatile var dummy: Any = _
  def timed[T](body: => T): Double = {
    val start = System.nanoTime
    /*
     * JVM中的某些运行时优化技术（如死代码消除），可能会去除body代码块的语句，是我们得到错误的运行时间。
     * 为了避免出现这种情况，可以将代码块body的返回值，赋予名为dummy的Volatile字段
     */

    dummy = body
    val end = System.nanoTime
    ((end - start) / 1000) / 1000.0
  }

  def warmedTimed[T](n: Int = 200)(body: => T): Double = {
    for (_ <- 0 until n) body
    timed(body)
  }

}
