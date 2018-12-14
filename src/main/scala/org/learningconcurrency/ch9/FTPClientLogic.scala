package org.learningconcurrency
package ch9


import java.io.File

import javax.swing.table.DefaultTableModel
import rx.lang.scala._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dialog}
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

  def updatePane(p: FilePane, dir: String, files: Seq[FileInfo]) = {
    val table = p.scrollPane.fileTable
    table.model match {
      case d: DefaultTableModel =>
        d.setRowCount(0)
        p.parent =
          if (dir == ".") "."
          else dir.take(dir.lastIndexOf(File.separator))
        p.dirFiles = files.sortBy(!_.isDir)
        for (f <- p.dirFiles) d.addRow(f.toRow)
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

  def setupPane(pane: FilePane): Unit = {
    val fileClicks = pane.table.rowDoubleClicks.map(row => pane.dirFiles(row))
    fileClicks.filter(_.isDir).subscribe {
      fileInfo =>
        pane.pathBar.filePath.text = pane.pathBar.filePath.text + File.separator + fileInfo.name
        refreshPane(pane)
    }
    pane.pathBar.upButton.clicks.subscribe { _ =>
      pane.pathBar.filePath.text = pane.parent
      refreshPane(pane)
    }
    def rowActions(button: Button): Observable[FileInfo] = button.clicks
      .map(_ => pane.table.peer.getSelectedRow)
      .filter(_ != -1)
      .map(row => pane.dirFiles(row))

    def setStatus(txt: String) = {
      status.label.text = txt
      refreshPane(files.leftPane)
      refreshPane(files.rightPane)
    }

    val rowCopies = rowActions(pane.buttons.copyButton)
      .map(info => (info, files.opposite(pane).currentPath))
    rowCopies.subscribe {
      t =>
        val (info, destDir) = t
        val dest = destDir + File.separator + info.name
        copyFile(info.path, dest) onComplete {
          case Success(s) =>
            swing {
              setStatus(s"File copied: $s")
            }
          case Failure(t) =>
            swing {
              setStatus(s"Could not copy file: $t")
            }

        }
    }

    val rowDeletes = rowActions(pane.buttons.deleteButton)
    rowDeletes.subscribe {
      info =>
        deleteFile(info.path) onComplete {
          case Success(s) =>
            swing { setStatus(s"File deleted: $s") }
          case Failure(t) =>
            swing { setStatus(s"Could not delete file: $t") }
        }
    }
  }

  setupPane(files.leftPane)
  setupPane(files.rightPane)

  menu.file.exit.reactions += {
    case ButtonClicked(_) =>
      system.stop(clientActor)
      system.terminate()
      sys.exit(0)
  }

  menu.help.about.reactions += {
    case ButtonClicked(_) =>
      Dialog.showMessage(message = "ScalaFTP version 0.1, made in Switzerland", title = "About ScalaFTP")
  }
}
