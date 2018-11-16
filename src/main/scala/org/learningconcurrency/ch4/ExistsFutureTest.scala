package org.learningconcurrency.ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.async.Async._

/**
  * 3. 使用exists方法扩展Future[T]类型，该方法接收一个判定函数并返回一个Future[Boolean]对象
  *
  * 只有当原Future对象被完善了，生成的Future对象才会由true完善，这样判定函数才会返回true，否则判定函数会返回false。
  * 你可以使用Future组合子，但是不许使用Promise对象。
  */
object ExistsFutureTest extends App {
//  implicit class ExistsFuture[T](future: Future[T]) {
//    def exists(p: T => Boolean): Future[Boolean] = for (value <- future) yield p(value)
//  }

//  implicit class ExistsFutureWithPromise[T](future: Future[T]) {
//    def exists(p: T => Boolean): Future[Boolean] = {
//      val promise = Promise[Boolean]
//      future.onComplete {
//        case Success(s) => promise.success(p(s))
//        case Failure(e) => promise.success(false)
//      }
//      promise.future
//    }
//  }

  implicit class ExistsFutureWithAsync[T](future: Future[T]) {
    def exists(p: T => Boolean): Future[Boolean] = {
      async {
        p(await(future))
      } recover { case _ => false }
    }
  }

  val hello = Future("hello")
  val world = Future("world")
  def startWithH(str:String) = str.startsWith("h")

  println(Await.result(hello.exists(startWithH),Duration.Inf))
  println(Await.result(world.exists(startWithH),Duration.Inf))
}
