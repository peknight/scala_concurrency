package org.learningconcurrency.ch5

import scala.collection.mutable.ArrayBuffer
import scala.collection.parallel.Combiner

class ParStringCombiner extends Combiner[Char, ParString] {

  private val chunks = new ArrayBuffer += new StringBuilder

  private var lastc = chunks.last

  var size = 0

  override def combine[N <: Char, NewTo >: ParString](other: Combiner[N, NewTo]): Combiner[N, NewTo] = {
    if (this eq other) this else other match {
      case that: ParStringCombiner =>
        size += that.size
        chunks ++= that.chunks
        lastc = chunks.last
        this
    }
  }

  override def +=(elem: Char): ParStringCombiner.this.type = {
    lastc append elem
    this
  }

  override def clear(): Unit = {
    chunks.clear()
    chunks ++ new StringBuilder
  }

  override def result(): ParString = {
    val rsb = new StringBuilder
    for (sb <- chunks) rsb.append(sb)
    new ParString(rsb.toString)
  }
}

//object ParStringCombinerTest extends App {
//  val txt = "A custom txt" * 25000
//  val partxt = new ParString(txt)
//  val seqtime = warmedTimed(250) { txt.filter(_ != ' ') }
//  val partime = warmedTimed(250) { partxt.filter(_ != ' ') }
//  import org.learningconcurrency.learningconcurrency._
//  log(s"Sequential time - $seqtime ms\n")
//  log(s"Parallel time - $partime ms\n")
//}