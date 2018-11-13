package org.learningconcurrency.ch3

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

/**
  * 实现一个TreiberStack类，通过该类实现并发堆内存抽象
  * Treiber: 驱动?
  * 应在该类中使用先前已经推入该堆栈的指向链接列表节点的原子变量引用。
  * 还应确保实现无所算法，并避开ABA问题
  *
  * @tparam T
  */
class TreiberStack[T] {
  private[this] val stack = new AtomicReference[List[T]](Nil)

  @tailrec final def push(x: T): Unit = {
    val origin = stack.get()
    if (!stack.compareAndSet(origin, x :: origin))
      push(x)
  }

  @tailrec final def pop(): T = {
    val origin = stack.get()
    if (stack.compareAndSet(origin, origin.tail)) origin.head else pop()
  }
}
