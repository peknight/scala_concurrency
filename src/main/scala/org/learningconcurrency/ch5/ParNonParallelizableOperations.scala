package org.learningconcurrency.ch5

import org.learningconcurrency.learningconcurrency._

import scala.collection.GenSeq

import scala.concurrent._
import ExecutionContext.Implicits.global

object ParNonParallelizableOperations extends App {
  ParHtmlSearch.getHtmlSpec() foreach { case specDoc =>
    def allMatches(d: GenSeq[String]) = warmedTimed() {
      val results = d.foldLeft("") { (acc, line) =>
        if (line.matches(".*TEXTAREA.*")) s"$acc\n$line" else acc
      }
    }
    val seqtime = allMatches(specDoc)
    log(s"Sequential time - $seqtime ms")

    val specPar = specDoc.par
    val partime = allMatches(specPar)
    log(s"Parallel time - $partime ms")

    specPar.aggregate("") (
      (acc, line) => if (line.matches(".*TEXTAREA.*")) s"$acc\n$line" else acc,
      (acc1, acc2) => acc1 + acc2
    )
  }

  Thread.sleep(10000)
}
