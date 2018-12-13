package org.learningconcurrency
package ch9

import scala.collection._
import scala.util.{Try, Success, Failure}
import scala.swing._
import scala.swing.event._
import javax.swing.table._
import javax.swing._
import javax.swing.border._
import java.awt.Color
import java.io.File
import rx.lang.scala._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import ch6._
import scala.swing.Swing._


trait FTPClientLogic {
  self: FTPClientFrame with FTPClientApi =>
  connected.onComplete {
    case Failure(t) =>
      swing {
        status.label.text = s"Could not connect: $t"
      }
  }
}
