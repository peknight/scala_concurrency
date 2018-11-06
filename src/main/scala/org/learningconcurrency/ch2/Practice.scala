package org.learningconcurrency.ch2

import java.util.concurrent.atomic.AtomicBoolean

import org.learningconcurrency.ch2.SynchronizedNesting.Account

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

object Practice {

  def parallel[A, B](a: => A, b: => B): (A, B) = {
    var valueA: Option[A] = None
    var valueB: Option[B] = None
    val threadA = thread{valueA = Some(a)}
    val threadB = thread{valueB = Some(b)}
    threadA.join()
    threadB.join()
    (valueA.get, valueB.get)
  }

  def periodically(duration: Long)(b: => Unit): Unit = {
    while (true) {
      Thread.sleep(duration)
      thread { b }
    }
  }

  class SyncVar[T] {

    private[this] var value: Option[T] = None

    def get(): T = synchronized {
      value match {
        case Some(x) => {
          value = None
          x
        }
        case None => throw new Exception("Empty SyncVar")
      }
    }

    def put(x: T): Unit = synchronized {
      value match {
        case Some(v) => throw new Exception("Occupied SyncVar" + v)
        case _ => value = Some(x)
      }
    }

    def isEmpty(): Boolean = if (value == None) true else false

    def nonEmpty(): Boolean = !isEmpty

    def getWait(): T = {
      @tailrec
      def getWaitRec(): T = {
        if (nonEmpty) {
          notify
          get
        } else {
          wait
          getWaitRec
        }
      }
      synchronized {
        getWaitRec()
      }
    }

    def putWait(x: T): Unit = {
      @tailrec
      def putWaitRec(x: T): Unit = {
        if (isEmpty) {
          notify
          value = Some(x)
        } else {
          wait
          putWaitRec(x)
        }
      }
      synchronized {
        putWaitRec(x)
      }
    }
  }

  class SyncQueue[T](val n: Int) {
    require(n >= 0)
    import collection.mutable.Queue
    private[this] val queue: Queue[T] = Queue.empty

    private[this] def isFull: Boolean = queue.length >= n

    def get(): T = synchronized {
      if (queue.isEmpty) {
        throw new Exception("Empty SyncQueue")
      } else {
        queue.dequeue()
      }
    }

    def put(x: T): Unit = synchronized {
      if (!isFull) {
        queue.enqueue(x)
      } else {
        throw new Exception("SyncQueue Full")
      }
    }

    def isEmpty(): Boolean = queue.isEmpty

    def nonEmpty(): Boolean = !queue.isEmpty

    def getWait(): T = {
      @tailrec
      def getWaitRec(): T = {
        if (nonEmpty) {
          notify
          get
        } else {
          wait
          getWaitRec
        }
      }
      synchronized {
        getWaitRec()
      }
    }

    def putWait(x: T): Unit = {
      @tailrec
      def putWaitRec(x: T): Unit = {
        if (!isFull) {
          notify
          queue.enqueue(x)
        } else {
          wait
          putWaitRec(x)
        }
      }
      synchronized {
        putWaitRec(x)
      }
    }
  }

  import SynchronizedDeadlock.send
  def sendAll(accounts: Set[Account], target: Account): Unit = {
    for (account <- accounts) {
      send(account, target, account.money)
    }
  }

  class PriorityTaskPool(val p: Int, val important: Int) {

    // 这个隐式参数一定要放在队列声明前面，要不然会死
    implicit val ord: Ordering[(Int, () => Unit)] = Ordering.by(_._1)

    private[this] val tasks = mutable.PriorityQueue[(Int, () => Unit)]()

    private[this] var shutdownFlag = false

    (for (_ <- 1 to p) yield new Worker).foreach(_.start)

    def asynchronous(priority: Int)(task: => Unit): Unit =
      if (!shutdownFlag) {
        tasks.synchronized {
          tasks.enqueue((priority, () => task))
          tasks.notify()
        }
      } else {
        throw new IllegalAccessException("PriorityTaskPool was shut down")
      }


    def shutdown(): Unit = {
      shutdownFlag = false
    }

    class Worker extends Thread {
      setDaemon(true)

      def poll() = tasks.synchronized {
        @tailrec def dequeue(): Option[(Int, () => Unit)] = {
          if (!tasks.isEmpty) {
            val task = tasks.dequeue()
            if (shutdownFlag && task._1 < important) None else Some(task)
          } else if (shutdownFlag) {
            None
          } else {
            tasks.wait()
            dequeue()
          }
        }

        dequeue()
      }

      @tailrec final override def run(): Unit = {
        poll() match {
          case Some((_, task)) => {task(); run()}
          case None =>
        }
      }
    }
  }

  def orderingDemo(): Unit = {
    import scala.util.Sorting
    val pairs = Array(("b",5,2),("c",3,1),("b",1,3),("a",6,2))

    Sorting.quickSort(pairs)(Ordering.by[(String,Int,Int),Int](_._3).reverse)
    pairs.foreach(println)

    Sorting.quickSort(pairs)(Ordering[(Int,String)].on(x => (x._3,x._1)))
    pairs.foreach(println)
  }

  def priorityDemo() = {
    implicit val ord: Ordering[(Int, String)] = Ordering.by(_._1)
    val priorityDemo = collection.mutable.PriorityQueue[(Int, String)]()
    priorityDemo.enqueue((2, "hello"))
    priorityDemo.enqueue((5, "ct"))
    priorityDemo.enqueue((1, "work"))
    priorityDemo.enqueue((3, "word"))
    (1 to priorityDemo.size).foreach(_ => println(priorityDemo.dequeue()))
  }

  def main(args: Array[String]): Unit = {
    // 练习1
    val x = "123"
    println(parallel(x * 3, x toInt))

    // 练习2
//    periodically(100)(println("Hello World"))

    // 练习3、4、5
    val syncVar = new SyncVar[String]
    var value: Seq[String] = Nil // 这个var想改成val应该用Future去实现
    val ta = thread {
      value = for (_ <- 0 to 15) yield {
        syncVar.getWait()
      }
    }
    val tb = thread {
      for (i <- 0 to 15) {
        syncVar.putWait(i.toString)
      }
    }
    ta.join()
    tb.join()
    println(value)

    orderingDemo()
    priorityDemo()

    val tasks = new PriorityTaskPool(10, 5)
    for (i <- 1 to 100) {
      val randInt = Random.nextInt(10)
      tasks.asynchronous(randInt){
        println(s"${Thread.currentThread.getName} My Weight is $randInt ${System.currentTimeMillis}")
      }
    }
    Thread.sleep(10000)
  }
}
