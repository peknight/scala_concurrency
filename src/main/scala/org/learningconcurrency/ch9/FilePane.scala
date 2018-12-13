package org.learningconcurrency
package ch9

import javax.swing.table.DefaultTableModel

import scala.swing._
import scala.swing.BorderPanel.Position._

class FilePane extends BorderPanel {
  object pathBar extends BorderPanel {
    val label = new Label("Path: ")
    val filePath = new TextField(".") {editable = false}
    val upButton = new Button("^")
    layout(label) = West
    layout(filePath) = Center
    layout(upButton) = East
  }
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
  object buttons extends GridPanel(1, 2) {
    val copyButton = new Button("Copy")
    val deleteButton = new Button("Delete")
    contents += copyButton += deleteButton
  }
  layout(pathBar) = North
  layout(scrollPane) = Center
  layout(buttons) = South

  var parent: String = "."
  var dirFiles : Seq[FileInfo] = Nil
  def table = scrollPane.fileTable
  def currentPath = pathBar.filePath.text
}
