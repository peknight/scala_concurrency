package org.learningconcurrency.ch5

import scala.collection.immutable
import scala.collection.parallel.SeqSplitter
import scala.collection.parallel.immutable.ParSeq

class ParString(val str: String) extends ParSeq[Char] {
  def apply(i: Int): Char = str.charAt(i)

  def splitter: SeqSplitter[Char] = new ParStringSplitter(str, 0, str.length)

  def length: Int = str.length

  def seq: immutable.Seq[Char] = new collection.immutable.WrappedString(str)
}
