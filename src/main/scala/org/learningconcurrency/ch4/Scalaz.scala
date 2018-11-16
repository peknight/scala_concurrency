package org.learningconcurrency.ch4

import org.learningconcurrency.learningconcurrency._
import scalaz.concurrent._

object Scalaz extends App {
  val tombola = Future {
    scala.util.Random.shuffle((0 until 10000).toVector)
//  }
  } unsafeStart

  tombola.unsafePerformAsync { numbers =>
    log(s"And the winner is: ${numbers.head}")
  }
  tombola.unsafePerformAsync { numbers =>
    log(s"... ahem, winner is: ${numbers.head}")
  }

  Thread.sleep(1000)
}
