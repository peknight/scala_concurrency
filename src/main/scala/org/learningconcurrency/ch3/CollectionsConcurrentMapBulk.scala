package org.learningconcurrency.ch3

import org.learningconcurrency.learningconcurrency._


object CollectionsConcurrentMapBulk extends App {
  /*
   * scala-execution-context-global-11: name: (Johnny,0)
   * scala-execution-context-global-11: name: (Jack,0)
   * scala-execution-context-global-11: name: (John 4,4)
   * scala-execution-context-global-11: name: (Jane,0)
   * scala-execution-context-global-11: name: (John 3,3)
   * scala-execution-context-global-11: name: (John 2,2)
   * scala-execution-context-global-11: name: (John 19,19)
   * scala-execution-context-global-11: name: (John 1,1)
   * scala-execution-context-global-11: name: (John 18,18)
   * scala-execution-context-global-11: name: (John 29,29)
   */
//  import java.util.concurrent.ConcurrentHashMap
//  import scala.collection.convert.decorateAsScala._
//  val names = new ConcurrentHashMap[String, Int]().asScala
//  names("Johnny") = 0; names("Jane") = 0; names("Jack") = 0
//  execute { for (n <- 0 until 30) names(s"John $n") = n }
//  execute { for (n <- names) log(s"name: $n") }
//  Thread.sleep(1000)

  import scala.collection._
  val names = new concurrent.TrieMap[String, Int]
  names("Janice") = 0; names("Jackie") = 0; names("Jill") = 0
  execute { for (n <- 10 until 100) names(s"John $n") = n }
  execute {
    log("snapshot time!")
    for (n <- names.map(_._1).toSeq.sorted) log(s"name: $n")
  }
  Thread.sleep(1000)
}
