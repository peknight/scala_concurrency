package org.learningconcurrency
package ch9

import scala.swing._
import scala.swing.BorderPanel.Position._

abstract class FTPClientFrame extends MainFrame {
  title = "ScalaFTP"
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
