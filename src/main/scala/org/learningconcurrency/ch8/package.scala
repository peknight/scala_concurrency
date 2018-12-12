package org.learningconcurrency

import akka.actor._

package object ch8 {
  lazy val ourSystem = ActorSystem("OurExampleSystem")
}
