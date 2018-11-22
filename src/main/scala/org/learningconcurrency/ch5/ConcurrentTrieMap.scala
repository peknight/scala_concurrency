package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._
import scala.collection._

object ConcurrentTrieMap extends App {
  val cache = new concurrent.TrieMap[Int, String]()
  for (i <- 0 until 100) cache(i) = i.toString
  for ((number, string) <- cache.par) cache(-number) = s"-$string"
  log(s"cache - ${cache.keys.toList.sorted}")
}
