package org.learningconcurrency
package ch4


import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.control.NonFatal

object PromisesCustomAsync extends App {
  def myFuture[T](b: => T): Future[T] = {
    val p = Promise[T]
    global.execute(() => try {
      p.success(b)
    } catch {
      case NonFatal(e) => p.failure(e)
    })
    p.future
  }
  val f = myFuture { "naa" + "na" * 8 + "Katamari Damacy!"}
  for (text <- f) log(text)
  Thread.sleep(1000)
}
