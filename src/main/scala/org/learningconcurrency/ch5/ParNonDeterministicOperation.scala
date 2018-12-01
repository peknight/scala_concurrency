package org.learningconcurrency
package ch5

import scala.concurrent._
import ExecutionContext.Implicits.global

object ParNonDeterministicOperation extends App {
  for (specDoc <- ParHtmlSearch.getHtmlSpec()) {
    val patt = ".*TEXTAREA.*"
    val seqresult = specDoc.find(_.matches(patt))
//    val parresult = specDoc.par.find(_.matches(patt))
    log(s"Sequential result - $seqresult")

    val index = specDoc.par.indexWhere(_.matches(patt))
    val parresult = if (index != -1) Some(specDoc(index)) else None
    log(s"Parallel result - $parresult")
  }
  Thread.sleep(10000)
}
