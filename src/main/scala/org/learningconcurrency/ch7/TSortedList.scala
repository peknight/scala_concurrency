package org.learningconcurrency
package ch7

import scala.concurrent.stm._

class TSortedList {
  val head = Ref[Node](null)
  override def toString: String = atomic {
    implicit txn =>
      val h = head()
      nodeToString(h)
  }

  import scala.annotation.tailrec
  @tailrec
  final def insert(n: Node, x: Int)(implicit txn: InTxn): Unit = {
    if (n.next() == null || n.next().elem > x) {
      n.append(new Node(x, Ref[Node](null)))
    } else insert(n.next(), x)
  }

  def insert(x: Int): this.type = atomic {
    implicit txn =>
//      @tailrec def insert(n: Node): Unit = {
//        if (n.next() == null || n.next().elem > x) {
//          n.append(new Node(x, Ref[Node](null)))
//        } else insert(n.next())
//      }

      if (head() == null || head().elem > x) {
        head() = new Node(x, Ref(head()))
      } else insert(head(), x)
      this
  }

}
object TSortedList extends App {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  val sortedList = new TSortedList
  val f = Future { sortedList.insert(1); sortedList.insert(4) }
  val g = Future { sortedList.insert(2); sortedList.insert(3) }
  for (_ <- f; _ <- g) log(s"sorted list - $sortedList")
  Thread.sleep(1000)

  val lst = new TSortedList
  lst.insert(4).insert(9).insert(1).insert(16)
  for (_ <- Future { pop(lst, 2) }) {
    log(s"removed 2 elements; list = $lst")
  }

  import scala.util.Failure
  Future { pop(lst, 3) } onComplete {
    case Failure(t) => log(s"shoa $t; list = $lst")
  }

  Thread.sleep(1000)

  Future {
    atomic {
      implicit txn =>
        pop(lst, 1)
        sys.error("")
    }
  } onComplete {
    case Failure(t) => log(s"oops again $t - $lst")
  }

  Thread.sleep(1000)

  import scala.util.control.Breaks._
  Future {
    breakable {
      atomic {
        implicit txn =>
          for (n <- List(1, 2, 3)) {
            pop(lst, n)
            break
          }
      }
    }
    log(s"after removing - $lst")
  }

  Thread.sleep(1000)


  import scala.util.control._
  Future {
    breakable {
      atomic.withControlFlowRecognizer {
        case c: ControlThrowable => false
      } {
        implicit txn =>
          for (n <- List(1, 2, 3)) {
            pop(lst, n)
            break
          }
      }
    }
    log(s"after removing - $lst")
  }

  Thread.sleep(1000)

  lst.insert(4).insert(9).insert(1)

  atomic {
    implicit txn =>
      pop(lst, 2)
      log(s"lst = $lst")
      try { pop(lst, 3) }
      catch { case e: Exception => log(s"Houston... $e!") }
      pop(lst, 1)
  }
  log(s"result - $lst")

  val queue1 = new TSortedList
  val queue2 = new TSortedList
  val consumer = Future {
    blocking {
      atomic {
        implicit txn =>
          log(s"probing queue1")
          log(s"got: ${headWait(queue1)}")
      } orAtomic {
        implicit txn =>
          log(s"probing queue2")
          log(s"got: ${headWait(queue2)}")
      }
    }
  }
  Thread.sleep(50)
  Future { queue2.insert(2) }
  Thread.sleep(50)
  Future { queue1.insert(1) }
  Thread.sleep(1000)

  val message = Ref("")
  Future {
    blocking {
      atomic.withRetryTimeout(1000) {
        implicit txn =>
          if (message() != "") log(s"got a message - ${message()}")
          else retry
      }
    }
  }
//  Thread.sleep(1024)
  Thread.sleep(500)
  message.single() = "Howdy!"
  Thread.sleep(1000)

  message.single() = ""
  Future {
    blocking {
      atomic {
        implicit txn =>
          if (message() == "") {
            retryFor(1000)
            log(s"no message")
          } else {
            log(s"got message - ${message()}")
          }
      }
    }
  }
  Thread.sleep(1000)

  val myList = new TSortedList().insert(14).insert(22)
  def clearWithLog(): String = atomic {
    implicit txn =>
      clearList(myList)
      myLog()
  }
  val f1 = Future { clearWithLog() }
  val g1 = Future { clearWithLog() }

  for (h1 <- f1; h2 <- g1) log(s"Log for f: $h1\nlog for g: $h2")

  Thread.sleep(1000)

//  val pages: Seq[String] = Seq.fill(5) ("Scala 2.10 is out, " * 7)
//  val website: Array[Ref[String]] = pages.map(Ref(_)).toArray

  val pages: Seq[String] = Seq.fill(5)("Scala 2.10 is out, " * 7)
  val website: TArray[String] = TArray(pages)

  def replace(p: String, s: String): Unit = atomic {
    implicit txn =>
      for (i <- 0 until website.length) {
        website(i) = website(i).replace(p, s)
      }
  }

  def asString = atomic {
    implicit txn =>
      var s: String = ""
      for (i <- 0 until website.length)
        s += s"Page $i\n======\n${website(i)}\n\n"
      s
  }

  val f2 = Future {
    replace("2.10", "2.11")
  }
  val g2 = Future {
    replace("out", "released")
  }
  for (_ <- f2; _ <- g2) log(s"Document\n$asString")

  Thread.sleep(1000)

  val alphabet = TMap("a" -> 1, "B" -> 2, "C" -> 3)
  Future {
    atomic {
      implicit txn =>
        alphabet("A") = 1
        alphabet.remove("a")
    }
  }
  Thread.sleep(23)
  Future {
    val snap = alphabet.single.snapshot
    log(s"atomic snapshot: $snap")
  }
  Thread.sleep(1000)
}
