package org.learningconcurrency
package ch3

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

class AtomicBuffer[T] {
  private val buffer = new AtomicReference[List[T]](Nil)
  @tailrec final def +=(x: T): Unit = {
    val xs = buffer.get
    val nxs = x :: xs
    if (!buffer.compareAndSet(xs, nxs)) this += x
  }
}
