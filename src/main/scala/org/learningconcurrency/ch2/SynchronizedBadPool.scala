package org.learningconcurrency.ch2

import org.learningconcurrency.learningconcurrency._
import scala.collection._

object SynchronizedBadPool extends App {
  private val tasks = mutable.Queue[() => Unit]()

  val worker = new Thread {
    def poll(): Option[() => Unit] = tasks.synchronized {
      if (tasks.nonEmpty) Some(tasks.dequeue()) else None
    }

    override def run(): Unit = {
      while (true) {
        poll() match {
          case Some(task) => task()
          case None =>
        }
      }
    }
  }

  worker.setName("Worker")
  worker.setDaemon(true)
  worker.start()

  def asynchronous(body: => Unit) = tasks.synchronized {
    tasks.enqueue(() => body)
  }

  asynchronous { log("Hello") }
  asynchronous { log(" world") }
  Thread.sleep(5000)
}
