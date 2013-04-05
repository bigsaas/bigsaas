package org.bigsaas.node

import java.net.InetAddress

import org.bigsaas.util.Config

object NodeConfig extends Config("bigsaas.node") {
  val name = stringOption("name") getOrElse InetAddress.getLocalHost.getHostName
  val nodes = stringList("nodes")
  val port = int("port") 
  val publicPorts = intRange("public-port-range")
  val privatePorts = intRange("private-port-range")
  val lastSignOfLifeSec = int("last-sign-of-life-sec")
  val workDir = string("work-dir")
}