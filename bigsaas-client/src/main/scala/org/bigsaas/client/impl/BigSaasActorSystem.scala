package org.bigsaas.client.impl

import akka.actor.ActorSystem
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.net.InetAddress
import com.typesafe.config.ConfigValue

class BigSaasActorSystem(actorPort : Int) {
  val ipAddress = InetAddress.getLocalHost();
  val s = s"dfkj{d}"
  val config = ConfigFactory.parseString(s"""
      akka {
  actor {
    provider = "akka.remote.RemoteActorRefProvider"
  }
  remote {
    transport = "akka.remote.netty.NettyRemoteTransport"
    netty {
      hostname = "127.0.0.1"
      port = {actorPort}
    }
 }
}""")
println(config)
  val system = ActorSystem("BigSaasSystem", config)

}