package org.learningconcurrency
package ch2

// 多个处理器无法总是以遍写程序的次序执行指令
// 线程无须将他们的所有更新内容立刻写入主内存区域，但他们可以暂时将这些内容缓存在处理器的寄存器中。
object ThreadSharedStateAccessReordering extends App {
  for (_ <- 0 until 100000) {
    var a = false
    var b = false
    var x = -1
    var y = -1
    val t1 = thread {
      a = true
      y = if (b) 0 else 1
    }
    val t2 = thread {
      b = true
      x = if (a) 0 else 1
    }
    t1.join()
    t2.join()
    assert(!(x == 1 && y == 1), s"x = $x, y = $y")
  }
}
