package org.learningconcurrency.ch3

import org.learningconcurrency.ch3
import org.learningconcurrency.learningconcurrency._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

/**
  * 1. 实现一个名为PiggybackContext的自定义ExecutionContext类，该类应能够通过调用execute方法的线程运行Runnable对象。
  * 应确保在PiggybackContext对象中运行的Runnable对象也能够调用execute方法，并能够以适当方式抛出异常
  */
class PiggybackContext extends ExecutionContext {
  def execute(task: Runnable): Unit = ch3.execute {
    Try(task.run()) match {
      case Success(_) => log("success")
      case Failure(e) => reportFailure(e)
    }
  }

  override def reportFailure(cause: Throwable): Unit = log(cause.getMessage)
}
object PiggybackContext extends App {
  val context = new PiggybackContext
  context.execute{ () => }
  context.execute{ () => throw new Exception("failure") }
  Thread.sleep(500)
}
