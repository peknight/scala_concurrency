package org.learningconcurrency.ch3

import org.learningconcurrency.learningconcurrency._
import java.util.concurrent.{ConcurrentHashMap, LinkedBlockingQueue}

import scala.collection.convert.decorateAsScala._
import scala.collection.concurrent
import java.io.File

import org.apache.commons.io.FileUtils

class FileSystem(val root: String) {
//  private val messages = new LinkedBlockingQueue[String]
//  val logger = new Thread {
//    setDaemon(true)
//    override def run(): Unit = while (true) log(messages.take())
//  }
//  logger.start()
//  def logMessage(msg: String): Unit = messages.offer(msg)

  val rootDir = new File(root)
  val files: concurrent.Map[String, Entry] = new ConcurrentHashMap[String, Entry]().asScala
  for (f <- FileUtils.iterateFiles(rootDir, null, false).asScala)
    files.put(f.getName, new Entry(false))

}

object FileSystem extends App {
//  val fileSystem = new FileSystem(".")
//  fileSystem.logMessage("Testing log")
//  Thread.sleep(500)
}
