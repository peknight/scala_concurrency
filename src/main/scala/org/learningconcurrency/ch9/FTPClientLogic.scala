package org.learningconcurrency
package ch9


import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util._


trait FTPClientLogic {
  self: FTPClientFrame with FTPClientApi =>
  connected.onComplete {
    case Failure(t) =>
      swing {
        status.label.text = s"Could not connect: $t"
      }
    case Success(false) =>
      swing { status.label.text = "Could not find server."}
    case Success(true) =>
      swing {
        status.label.text = "Connected!"
        refreshPane(files.leftPane)
        refreshPane(files.rightPane)
      }
  }
  def refreshPane(pane: FilePane): Unit = {
    val dir = pane.pathBar.filePath.text
    getFileList(dir) onComplete {
      case Success((dir, files)) =>
        swing { updatePane(pane, dir, files) }
      case Failure(t) =>
        swing { status.label.text = s"Could not update pane: $t"}
    }
  }
}
