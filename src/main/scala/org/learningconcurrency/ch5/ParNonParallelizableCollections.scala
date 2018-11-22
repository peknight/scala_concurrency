package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._

object ParNonParallelizableCollections extends App {
  val list = List.fill(1000000)("")
  val vector = Vector.fill(1000000)("")
  log(s"list conversion time: ${timed(list.par)} ms")
  log(s"vector conversion time: ${timed(vector.par)} ms")
}
