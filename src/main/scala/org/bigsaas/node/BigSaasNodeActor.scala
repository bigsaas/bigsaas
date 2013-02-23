package org.bigsaas.node

import org.bigsaas.client.impl.BigSaasClientImpl
import akka.actor.Actor
import org.bigsaas.util.Logging
import org.bigsaas.core.RuntimeInfoRequest
import org.bigsaas.core.RuntimeInfo
import org.bigsaas.core.NodeInfo
import org.bigsaas.core.BigSaasConfig
import scala.concurrent.duration.Deadline
import org.bigsaas.core.ApplicationSignOfLife
import collection.mutable
import org.bigsaas.core.ApplicationNodeInfo
import scala.concurrent.duration._
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequest
import scala.util.Left
import scala.util.Success
import scala.util.Failure
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.bigsaas.core.NodeSignOfLife


/**
 * A node in the BigSaas cluster.
 */
class BigSaasNodeActor extends Actor with Logging {

  import context.dispatcher 
  
  info("BigSaas Node started")
  
  val elasticSearchNode = new ElasticSearchNode
  val esClient = elasticSearchNode.esClient
  
  val nodeInfo = mutable.Map[(String, String, Int), NodeInfo]()
  val applicationNodeInfo = mutable.Map[(String, String, Int), ApplicationNodeInfo]()
  def runtimeInfo = RuntimeInfo(nodeInfo.values.toList ++ nodeInfo.values.toList)

  // Periodically ask ES for nodes and send last sign of life messages to them
  context.system.scheduler.schedule(0 seconds, BigSaasConfig.lastSignOfLifeSec seconds) {
    esClient.execute(new NodesInfoRequest).onComplete {
      case Success(response) =>
        response.getNodes.foreach { node =>
          val host = node.node.address match {
            case inet : InetSocketTransportAddress => inet.address.getHostString
            case _ => ""
          }
          val nodeActor = context.actorFor(s"akka://bigsaas@$host:${BigSaasConfig.nodePort}/user/nodeActor")
          nodeActor ! NodeSignOfLife(BigSaasConfig.nodeName, BigSaasConfig.ip, BigSaasConfig.nodePort, applicationNodeInfo.values.toList)
        }
      case Failure(e) => error("Lost connection with ElasticSearch: " + e)
    }
  }
  
  def receive = {
    case "info" => info("Received INFO command")
    case RuntimeInfoRequest => sender ! runtimeInfo
    case NodeSignOfLife(name, ip, port, applications) =>
      nodeInfo += (name, ip, port) -> NodeInfo(name, ip, port, applications, Deadline.now)
    case ApplicationSignOfLife(name, version, clientPort, message) =>
      applicationNodeInfo += (name, version, clientPort) -> ApplicationNodeInfo(name, version, clientPort, Deadline.now)
    case s => info("Received Other command" + s)
  }
}