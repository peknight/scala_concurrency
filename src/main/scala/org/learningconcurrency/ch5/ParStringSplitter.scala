package org.learningconcurrency
package ch5

import scala.collection.parallel.SeqSplitter

class ParStringSplitter(val s: String, var i: Int, val limit: Int) extends SeqSplitter[Char] {
  override def dup: SeqSplitter[Char] = new ParStringSplitter(s, i, limit)

  override def split: Seq[SeqSplitter[Char]] = {
    val rem = remaining
    if (rem >= 2) psplit(rem / 2, rem - rem / 2)
    else Seq(this)
  }

  override def psplit(sizes: Int*): Seq[SeqSplitter[Char]] = {
    val ss = for (sz <- sizes) yield {
      val nlimit = (i + sz) min limit
      val ps = new ParStringSplitter(s, i, nlimit)
      i = nlimit
      ps
    }
    if (i == limit) ss
    else ss :+ new ParStringSplitter(s, i, limit)
  }

  override def remaining: Int = limit - i

  final def hasNext: Boolean = i < limit

  final def next(): Char = {
    val r = s.charAt(i)
    i += 1
    r
  }
}
