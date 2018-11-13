package org.learningconcurrency.ch3

import java.util.concurrent.atomic.AtomicReference

class Entry(val isDir: Boolean) {
  val state = new AtomicReference[State](new Idle)
}
