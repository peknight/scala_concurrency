package org.learningconcurrency
package ch9

sealed trait State {
  def inc: State
  def dec: State
}

case object Created extends State {
  def inc = sys.error("File being created.")
  def dec = sys.error("File being created.")
}

case object Idle extends State {
  def inc = Copying(1)
  def dec = sys.error("Idle not copied.")
}

case object Deleted extends State {
  def inc = sys.error("Cannot copy deleted.")
  def dec = sys.error("Deleted not copied.")
}

case class Copying(n: Int) extends State {
  def inc: State = Copying(n + 1)
  def dec: State = if (n > 1) Copying(n - 1) else Idle
}

