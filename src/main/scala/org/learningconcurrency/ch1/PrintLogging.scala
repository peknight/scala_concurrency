package org.learningconcurrency
package ch1

class PrintLogging extends Logging {
  def log(s: String) = println(s)
}
