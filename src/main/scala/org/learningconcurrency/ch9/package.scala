package org.learningconcurrency

import java.awt.event.MouseAdapter
import java.time.format.DateTimeFormatter

import scala.swing._
import rx.lang.scala._

import scala.swing.event.ButtonClicked

package object ch9 {
  val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
  def swing(body: =>Unit) = {
    val r = new Runnable {
      def run() = body
    }
    javax.swing.SwingUtilities.invokeLater(r)
  }

  implicit class ButtonOps(val self: Button) {
    def clicks = Observable.create[Unit] {
      obs =>
        self.reactions += {
          case ButtonClicked(_) => obs.onNext(())
        }
        Subscription()
    }
  }

  implicit class TableOps(val self: Table) {
    def rowDoubleClicks = Observable[Int] {
      sub =>
        self.peer.addMouseListener(new MouseAdapter {
          override def mouseClicked(e: java.awt.event.MouseEvent): Unit = {
            if (e.getClickCount == 2) {
              val row = self.peer.getSelectedRow
              sub.onNext(row)
            }
          }
        })
    }
  }
}
