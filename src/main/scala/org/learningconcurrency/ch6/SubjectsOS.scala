package org.learningconcurrency
package ch6

object SubjectsOS extends App {
  log(s"RxOS boot sequence starting...")
  val loadedModules = List(TimeModule.systemClock, FileSystemModule.fileModifications).map(_.subscribe(RxOS.messageBus))
  log(s"RxOS boot sequence finished!")
  Thread.sleep(10000)
  for (mod <- loadedModules) mod.unsubscribe()
  log(s"RxOS dumping the complete system event log")
  // log _ -> logToFile
  RxOS.messageLog.subscribe(log _)
  log(s"RxOS going for shutdown")
}
