package org.learningconcurrency
package ch4

import scala.concurrent._
import ExecutionContext.Implicits.global
import java.io._
import org.apache.commons.io.FileUtils._
import scala.collection.convert.decorateAsScala._
import scala.io._

object FuturesClumsyCallback extends App {
  def blacklistFile(name: String): Future[List[String]] = Future {
    val lines = Source.fromFile(name).getLines
    lines.filter(x => !x.startsWith("#") && !x.isEmpty).toList
  }

  /**
    * 将格式列表提交给该方法后，该方法可以找到当前目录中符合这些格式的所有文件。
    * Apache Commons IO开源包中的iterateFiles方法，会返回这些项目文件的额Java迭代器，因此可以通过调用asScala方法将之转换为Scala迭代器。
    * 然后我们就可以获得所有匹配的文件路径
    * @param patterns
    * @return
    */
  def findFiles(patterns: List[String]): List[String] = {
    val root = new File(".")
    for {
      f <- iterateFiles(root, null, true).asScala.toList
      pat <- patterns
      abspat = root.getCanonicalPath + File.separator + pat
      if f.getCanonicalPath.contains(abspat)
    } yield f.getCanonicalPath
  }

  def blacklisted(name: String): Future[List[String]] =
    for (patterns <- blacklistFile(name)) yield findFiles(patterns)

  for (lines <- blacklistFile(".gitignore"))
    log(s"matches: ${findFiles(lines).mkString("\n")}")

  Thread.sleep(1000)

  log("----------")

  val buildFile = Future { Source.fromFile("build.sbt").getLines }
  val longest = for (ls <- buildFile) yield ls.maxBy(_.length)
  for (line <- longest) {
    log(s"longest line: $line")
  }

  Thread.sleep(1000)

  log("----------")

  val netiquetteUrl = "http://www.ietf.org/rfc/rfc1855.txt"
  val netiquette = Future { Source.fromURL(netiquetteUrl).mkString }
  val urlSpecUrl = "http://www.w3.org/Addressing/URL/url-spec.txt"
  val urlSpec = Future { Source.fromURL(urlSpecUrl).mkString }

  val netiquetteRecover = netiquette recover {
    case e: java.io.FileNotFoundException =>
      "Dear secretary, thank you for your e-mail." +
      "You might be interested to know that ftp links " +
      "can also point to regular files we keep on our servers."
  }

  val answer = for {
    nettext <- netiquetteRecover
    urltext <- urlSpec
  } yield s"Check this out: $nettext. And check out: $urltext"

  for (contents <- answer) log(contents)

  Thread.sleep(10000)
}
