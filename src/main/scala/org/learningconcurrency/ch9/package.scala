package org.learningconcurrency

import java.time.format.DateTimeFormatter

package object ch9 {
  val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
  def swing(body: =>Unit) = {
    val r = new Runnable {
      def run() = body
    }
    javax.swing.SwingUtilities.invokeLater(r)
  }
}
