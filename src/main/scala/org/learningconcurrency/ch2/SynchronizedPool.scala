package org.learningconcurrency
package ch2

import scala.annotation.tailrec
import scala.collection._

object SynchronizedPool extends App {
  private val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {
    var terminated = false

    def poll() = tasks.synchronized {
      while (tasks.isEmpty && !terminated) tasks.wait()
      if (!terminated) Some(tasks.dequeue()) else None
    }

    @tailrec override def run(): Unit = {
      poll() match {
        case Some(task) => task(); run()
        case None =>
      }
    }
    def shutdown() = tasks.synchronized {
      terminated = true
      tasks.notify()
    }
  }

  Worker.setDaemon(true)
  Worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue(() => body)
    tasks.notify()
  }

  asynchronous { log("Hello") }
  asynchronous { log("World!") }
  Thread.sleep(500)
}
