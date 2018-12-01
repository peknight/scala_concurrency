package org.learningconcurrency
package ch4

import java.util.concurrent.ConcurrentHashMap

import scala.concurrent._
import ExecutionContext.Implicits.global

/**
  * 应使键-值对能够被添加到IMap对象中，并使它们永远无法被删除和修改。特定的键只能被赋值一次，以后再调用该键中的update方法，
  * 会导致程序抛出异常。调用特定键中的apply方法会返回一个Future对象，当该键被插入到映射中后该Future对象会被完善。除了Future和Promise对象，
  * 你还可以使用scala.concurrent.Map类
  * @tparam K
  * @tparam V
  */
class IMap[K, V] {
  private[this] val map = new ConcurrentHashMap[K, Promise[V]]
  def update(k: K, v: V): Unit = {
    if (!map.containsKey(k)) map.putIfAbsent(k, Promise[V])
    map.get(k).success(v)
  }

  def apply(k: K): Future[V] = {
    if (!map.containsKey(k)) map.putIfAbsent(k, Promise[V])
    map.get(k).future
  }
}
