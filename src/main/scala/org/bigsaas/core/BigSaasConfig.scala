
package org.bigsaas.core

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import org.bigsaas.util.ConfigUtils
import com.typesafe.config.impl.ConfigInt
import com.typesafe.config.impl.ConfigString
import java.net.InetAddress
import com.typesafe.config.ConfigException
import collection.JavaConversions._
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigValueType

class BigSaasConfig {
  val original = ConfigFactory.load
  val applicationName = original.getString("bigsaas.application.name")
  val applicationVersion = original.getString("bigsaas.application.version")
  val nodePort = original.getInt("bigsaas.node.port")
  val lastSignOfLifeSec = original.getInt("bigsaas.last-sign-of-life-sec")
  val startNode = original.getBoolean("bigsaas.start-node")
  val nodeName = original.getString("bigsaas.node.name") match {
    case "" => InetAddress.getLocalHost.getHostName
    case s => s
  }
  val ip = InetAddress.getLocalHost.getHostAddress
  val nodes = list("bigsaas.node.nodes")
  val dataNodes = list("bigsaas.node.data-nodes")
  val config = ConfigFactory.parseString(s"""
      akka.actor.provider = "akka.remote.RemoteActorRefProvider"
      akka.remote.transport = "akka.remote.netty.NettyRemoteTransport"
      akka.remote.netty.hostname = "localhost"
      akka.remote.netty.port = $nodePort
      bigsaas.node.name = "$nodeName"
      bigsaas.node.ip = "$ip"
      bigsaas.node.data-nodes = [${dataNodes.mkString("\"", ",", "\"")}]
   """).withFallback(original)

  val bigSaasConfig = config.getConfig("bigsaas")
  val nodeConfig = bigSaasConfig.getConfig("node")
  
  private def list(key: String) = original.getValue(key) match {
      case s if s.valueType == ConfigValueType.STRING => original.getString(key).replaceAll("\\\"|\\[|\\]", "").split(",").toList
      case s if s.valueType == ConfigValueType.LIST => original.getList(key).toList
  }
}

object BigSaasConfig extends BigSaasConfig {
  implicit def toConfig(config : BigSaasConfig) : Config = config.config
}