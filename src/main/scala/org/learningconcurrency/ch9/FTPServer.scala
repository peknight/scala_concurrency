package org.learningconcurrency
package ch9

object FTPServer extends App {
  val fileSystem = new FileSystem(".")
  fileSystem.init()
  val port = args(0).toInt
  val actorSystem = ch8.remotingSystem("FTPServerSystem", port)
  actorSystem.actorOf(FTPServerActor(fileSystem), "server")
}
