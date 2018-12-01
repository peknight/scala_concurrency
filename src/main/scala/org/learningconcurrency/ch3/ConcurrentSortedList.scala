package org.learningconcurrency
package ch3

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

/**
  * 3. 实现一个ConcurrentSortedList类，通过该类实现一个并发已排序列表抽象
  * 在ConcurrentSortedList类内部，应使用原子变量引用的链接列表。应确保实现无锁算法并避开ABA问题
  *
  * 由iterator方法返回的iterator对象，必须能够在列表中的add方法没有被以并发方式调用的情况下，通过升序正确遍历列表中的元素。
  *
  * 4.修改上一个练习创建的ConcurrentSortedList类，以线性方式调用该列表中的add方法，并在每次以并发方式调用add方法都能成功的前提下，
  * 为新建对象创建一个常数 // 没看懂 不做 可能是翻译的太差劲了？
  */
class ConcurrentSortedList[T](implicit val ord: Ordering[T]) {
  private[this] val sortedList = new AtomicReference[List[T]](Nil)

  @tailrec final def add(x: T): Unit = {
    val list = sortedList.get()
    if (!sortedList.compareAndSet(list, (x :: list).sorted(ord))) add(x)
  }
  def iterator: Iterator[T] = {
    sortedList.get().iterator
  }
}
