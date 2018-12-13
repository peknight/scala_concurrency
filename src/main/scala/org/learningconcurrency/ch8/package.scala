package org.learningconcurrency

import akka.actor._

package object ch8 {
  lazy val ourSystem = ActorSystem("OurExampleSystem")
  import com.typesafe.config._
  def remotingConfig(port: Int) = ConfigFactory.parseString(
    s"""
       |akka {
       |  actor.provider = "akka.remote.RemoteActorRefProvider"
       |  remote {
       |    enable-transports = ["akka.remote.netty.tcp"]
       |    netty.tcp {
       |      hostname = "127.0.0.1"
       |      port = $port
       |    }
       |  }
       |}
     """.stripMargin)
  def remotingSystem(name: String, port: Int): ActorSystem =
    ActorSystem(name, remotingConfig(port))
}
