package org.learningconcurrency
package ch6

import rx.lang.scala._

object Exercise4 extends App {

  /**
    * 向Observable[T]类型添加toSignal方法，该方法应将Observable对象转换为响应式信号
    */
  class Signal[T] {
    def apply(): T = ???
    def map[S](f: T => S): Signal[S] = ???
    def zip[S](that: Signal[S]): Signal[(T, S)] = ???
    def scan[S](z: S)(f: (S, T) => S) = ???
  }

  implicit class ObservableOps[T](Observable: Observable[T]) {
    def toSignal: Signal[T] = ???
  }



}
