package org.learningconcurrency
package ch9

sealed trait FileEvent

case class FileCreated(path: String) extends FileEvent
case class FileDeleted(path: String) extends FileEvent
case class FileModified(path: String) extends FileEvent
