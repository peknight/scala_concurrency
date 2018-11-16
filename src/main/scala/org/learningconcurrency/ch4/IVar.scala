package org.learningconcurrency.ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * 2. 使用IVar类实现一个单一赋值变量抽象
  *
  * 在被创建时，IVar对象不会含有值，而且调用其中的apply方法会导致程序抛出异常。
  * 使用:=方法为该对象赋值后，再次调用:=方法也会使程序跑出异常，而调用该对象中的apply方法会获得该对象先前被赋予的值。
  * 只许使用Future和Promise对象，而且不许使用前面几章介绍的异步基元。
  */
class IVar[T] {
  private val promise = Promise[T]
  def apply(): T = {
    if (promise.isCompleted) Await.result(promise.future, Duration.Inf) else throw new IllegalStateException
  }
  def :=(x: T): Unit = {
    promise.success(x)
  }
}
