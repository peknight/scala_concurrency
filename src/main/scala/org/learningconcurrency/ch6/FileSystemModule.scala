package org.learningconcurrency
package ch6

import ObservablesSubScriptions._

object FileSystemModule {
  val fileModifications = modified(".")
}
