package org.bigsaas.client.impl

import org.bigsaas.client.BigSaasClient
import org.bigsaas.client.elasticsearch.ESClient
import org.elasticsearch.client.Client
import org.bigsaas.core.Tenant
import akka.actor.ActorSystem
import org.bigsaas.core.BigSaasConfig
import com.typesafe.config.Config
import org.bigsaas.util.SocketUtils
import org.bigsaas.core.NoPortAvailableException
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import org.bigsaas.core.ApplicationSignOfLife
import org.bigsaas.node.BigSaasNode

class BigSaasClientImpl extends BigSaasClient {

  // Get application info
  val applicationName = BigSaasConfig.applicationName
  val applicationVersion = BigSaasConfig.applicationVersion
  
  // Start an embedded bigsaas node
  if (BigSaasConfig.startNode) {
    BigSaasNode.startIfNotRunning
  }
  
  // Start the akka system.
  val nodePort = BigSaasConfig.nodePort
  val clientPort = SocketUtils.findPort(nodePort + 1, 1000) match {
    case Some(port) => port
    case None => throw new NoPortAvailableException("No client akka port available")
  }
  val clientConfig = ConfigFactory.parseString(s"""
      akka.remote.netty.port = $clientPort
   """).withFallback(BigSaasConfig)
  val system = ActorSystem("bigsaas", clientConfig)

  // Notify node actor that we are here. 
  val nodeActor = system.actorFor(s"akka://bigsaas@localhost:$nodePort/user/nodeActor")  
  system.scheduler.schedule(0 seconds, BigSaasConfig.lastSignOfLifeSec seconds) {
    nodeActor ! ApplicationSignOfLife(applicationName, applicationVersion, clientPort, None)
  }
  
  def version = 1
  def httpPort = 80
  
  def elasticSearchClient(tenant : Tenant) : ESClient = new ESClient(null.asInstanceOf[Client]) 

  case object ApplicationInfoRequest
  case class ApplicationInfoResponse(platformVersion : Int, httpPort : Option[Int], httpsPort : Option[Int])
  
  lazy val admin = new BigSaasAdminClientImpl(system, nodeActor)
}