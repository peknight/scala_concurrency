package org.learningconcurrency.ch3

import org.learningconcurrency.learningconcurrency._
import java.util.concurrent.atomic.AtomicLong

object AtomicUid extends App {
  private val uid = new AtomicLong(0L)
  def getUniqueId(): Long = uid.incrementAndGet()
  execute { log(s"Uid asynchronously: ${getUniqueId()}") }
  log(s"Got a unique id: ${getUniqueId()}")
}
