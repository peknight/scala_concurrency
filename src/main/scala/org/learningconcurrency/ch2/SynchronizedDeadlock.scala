package org.learningconcurrency.ch2

import org.learningconcurrency.learningconcurrency._

object SynchronizedDeadlock extends App {
  import SynchronizedNesting.Account
  def send(a1: Account, a2: Account, n: Int) = {
    def adjust(): Unit = {
      a1.money -= n
      a2.money += n
    }
    if (a1.uid < a2.uid)
      a1.synchronized { a2.synchronized { adjust() } }
    else
      a2.synchronized { a1.synchronized { adjust() } }
  }

  val a = new Account("Jack", 1000)
  val b = new Account("Jill", 2000)
  val t1 = thread { for (i <- 0 until 100) send(a, b, 1) }
  val t2 = thread { for (i <- 0 until 100) send(b, a, 1) }
  t1.join()
  t2.join()
  log(s"a = ${a.money}, b = ${b.money}")
}
