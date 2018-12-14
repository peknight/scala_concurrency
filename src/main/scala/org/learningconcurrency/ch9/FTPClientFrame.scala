package org.learningconcurrency
package ch9

import java.awt.Color

import javax.swing.BorderFactory
import javax.swing.table.DefaultTableModel

import scala.swing._

abstract class FTPClientFrame extends MainFrame {
  import scala.swing.BorderPanel.Position._

  title = "ScalaFTP"

  class FilePane extends BorderPanel {
    object pathBar extends BorderPanel {
      val label = new Label("Path: ")
      val filePath = new TextField(".") {
        border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)
        editable = false
      }
      val upButton = new Button("^")
      layout(label) = West
      layout(filePath) = Center
      layout(upButton) = East
      border = BorderFactory.createEmptyBorder(2, 2, 2, 2)
    }
    layout(pathBar) = North

    object scrollPane extends ScrollPane {
      val columnNames = Array[AnyRef]("Filename", "size", "Date modified")
      val fileTable = new Table {
        showGrid = true
        model = new DefaultTableModel(columnNames, 0) {
          override def isCellEditable(row: Int, column: Int): Boolean = false
        }
        selection.intervalMode = Table.IntervalMode.Single
      }
      contents = fileTable
    }
    layout(scrollPane) = Center

    object buttons extends GridPanel(1, 2) {
      val copyButton = new Button("Copy")
      val deleteButton = new Button("Delete")
      contents += copyButton += deleteButton
    }
    layout(buttons) = South

    var parent: String = "."
    var dirFiles : Seq[FileInfo] = Nil

    def table = scrollPane.fileTable

    def currentPath = pathBar.filePath.text
  }

  object menu extends MenuBar {
    object file extends Menu("File") {
      val exit = new MenuItem("Exit ScalaFTP")
      contents += exit
    }
    object help extends Menu("Help") {
      val about = new MenuItem("About...")
      contents += about
    }
    contents += file += help
  }

  object status extends BorderPanel {
    val label = new Label("connection...", null, Alignment.Left)
    layout(new Label("Status: ")) = West
    layout(label) = Center
  }

  object files extends GridPanel(1, 2) {
    val leftPane = new FilePane
    val rightPane = new FilePane
    contents += leftPane += rightPane

    def opposite(pane: FilePane) = if (pane eq leftPane) rightPane else leftPane
  }

  contents = new BorderPanel {
    layout(menu) = North
    layout(files) = Center
    layout(status) = South
  }
}
