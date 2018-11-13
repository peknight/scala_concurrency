package org.learningconcurrency.ch3

import java.util.concurrent.atomic.AtomicReference

/**
  * 5. 创建一个LazyCell对象并调用apply方法，在编写这些代码时必须使用与惰性值相同的语义
  *
  * 不许在实现代码中使用惰性值。
  *
  * 6. 使用与上例LazyCell类相同的接口和语义，实现一个PureLazyCell类。应在不受参数初始化操作影响的前提下，
  * 使PureLazyCell类能够被多次求值。 ???
  */
class LazyCell[T](initialization: => T) {
  private[this] val value = new AtomicReference[Option[T]](None)

  def apply(): T = {
    val origin = value.get()
    origin match {
      case Some(_: T) => origin.get
      case None => if (value.compareAndSet(origin, Some(initialization))) value.get.get else apply
    }
  }
}
