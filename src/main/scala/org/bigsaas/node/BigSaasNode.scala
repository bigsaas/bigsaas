package org.bigsaas.node

import org.bigsaas.core.BigSaasConfig
import akka.actor.ActorSystem
import akka.actor.Props
import org.bigsaas.util.SocketUtils
import org.bigsaas.core.NodeAlreadyRunningException
import org.bigsaas.util.Logging

object BigSaasNode extends Logging {
  lazy val system = ActorSystem("bigsaas", BigSaasConfig)
  lazy val isAlreadyRunning = !SocketUtils.portAvailable(BigSaasConfig.nodePort)
  private lazy val node = system.actorOf(Props[BigSaasNodeActor], "nodeActor")
  def start {
    if (BigSaasNode.isAlreadyRunning) {
     throw new NodeAlreadyRunningException(s"There is already a BigSaas node running on port ${BigSaasConfig.nodePort} (${BigSaasConfig.nodeName})")
    }
    node
  }
  def startIfNotRunning = {
    if (!isAlreadyRunning) {
      node
    }
  }
}