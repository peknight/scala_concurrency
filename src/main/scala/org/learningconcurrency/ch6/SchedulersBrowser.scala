package org.learningconcurrency.ch6

import scala.swing.SimpleSwingApplication

object SchedulersBrowser extends SimpleSwingApplication {
  def top = new BrowserFrame with BrowserLogic
}
