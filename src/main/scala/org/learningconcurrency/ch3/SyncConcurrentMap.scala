package org.learningconcurrency
package ch3

import scala.collection.concurrent.Map
import scala.collection.mutable

class SyncConcurrentMap[A, B] extends Map[A, B] {
  private[this] val syncMap = mutable.Map.empty[A, B]

  override def putIfAbsent(k: A, v: B): Option[B] = syncMap.synchronized {
    syncMap.get(k) match {
      case someValue @ Some(_) => someValue
      case _ => syncMap(k) = v; None
    }
  }

  override def remove(k: A, v: B): Boolean = syncMap.synchronized {
    syncMap.remove(k) match {
      case Some(_) => true
      case _ => false
    }
  }

  override def replace(k: A, oldvalue: B, newvalue: B): Boolean = syncMap.synchronized {
    syncMap.get(k) match {
      case Some(`oldvalue`) => syncMap(k) = newvalue; true
      case _ => false
    }
  }

  override def replace(k: A, v: B): Option[B] = syncMap.synchronized {
    syncMap.get(k) match {
      case someValue @ Some(_) => syncMap(k) = v; someValue
      case _ => None
    }
  }

  override def +=(kv: (A, B)): SyncConcurrentMap.this.type = syncMap.synchronized {
    syncMap += kv
    this
  }

  override def -=(key: A): SyncConcurrentMap.this.type = syncMap.synchronized {
    syncMap -= key
    this
  }

  override def get(key: A): Option[B] = syncMap.synchronized {
    syncMap.get(key)
  }

  override def iterator: Iterator[(A, B)] = syncMap.synchronized {
    syncMap.iterator
  }
}
