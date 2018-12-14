package org.learningconcurrency
package ch9

import java.io._
import java.time.{Instant, ZoneId}

import org.apache.commons.io.FileUtils

case class FileInfo(path: String, name: String, parent: String, modified: String, isDir: Boolean, size: Long, state: State) {
  def toRow = Array[AnyRef](name, if (isDir) "" else size / 1000 + "kB", modified)
  def toFile = new File(path)
}

object FileInfo {
  def apply(file: File): FileInfo = {
    val path = file.getPath
    val name = file.getName
    val parent = file.getParent
    val modified = dateFormat.format(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()))
    val isDir = file.isDirectory
    val size = if (isDir) -1 else FileUtils.sizeOf(file)
    FileInfo(path, name, parent, modified, isDir, size, Idle)
  }
  def creating(file: File, size: Long): FileInfo = {
    val path = file.getPath
    val name = file.getName
    val parent = file.getParent
    val modified = dateFormat.format(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()))
    val isDir = false
    FileInfo(path, name, parent, modified, isDir, size, Created)
  }
}
