package org.learningconcurrency

import scala.concurrent.ExecutionContext

package object ch3 {
  def execute(body: => Unit) = ExecutionContext.global.execute(() => body)
}
