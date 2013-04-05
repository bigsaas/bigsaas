package org.bigsaas.node.admin

import java.net.InetAddress

import org.bigsaas.util.Config

object AdminConfig extends Config("bigsaas.node.admin") {
  val port = int("port") 
}