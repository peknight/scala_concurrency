package org.learningconcurrency.ch2

import org.learningconcurrency.learningconcurrency._

object ThreadsCommunicate extends App {
  var result: String = null
  val t = thread { result = "\nTitle\n" + "=" * 5 }
  t.join()
  log(result)
}
