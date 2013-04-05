package org.bigsaas.client.impl

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Deadline
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import org.bigsaas.client.ApplicationConfig
import org.bigsaas.client.BigSaasClient
import org.bigsaas.client.elasticsearch.ESClient
import org.bigsaas.core.ActiveApplication
import org.bigsaas.core.ActiveApplicationStatus
import org.bigsaas.core.NoPortAvailableException
import org.bigsaas.core.Tenant
import org.bigsaas.node.BigSaasNode
import org.bigsaas.node.NodeConfig
import org.bigsaas.util.Config
import org.bigsaas.util.SocketUtils
import org.bigsaas.util.model.Id
import org.elasticsearch.client.Client
import akka.actor.ActorSystem
import akka.actor.Props
import org.joda.time.DateTime

class BigSaasClientImpl extends BigSaasClient {

  // Get application info
  val applicationId = Id.generate[ActiveApplication]
  
 // Start an embedded bigsaas node
  BigSaasNode.startIfNotRunning
  
  // Start the akka system.
  val nodePort = NodeConfig.port
  val clientPort = SocketUtils.findPort(NodeConfig.privatePorts) match {
    case Some(port) => port
    case None => throw new NoPortAvailableException("No client akka port available")
  }
  val actorSystem = ActorSystem("bigsaas", Config.merge(s"""
      akka.actor.provider = "akka.remote.RemoteActorRefProvider"
      akka.remote.transport = "akka.remote.netty.NettyRemoteTransport"
      akka.remote.netty.hostname = "${SocketUtils.ip}"
      akka.remote.netty.port = $clientPort
   """))

  // Get a reference to the node actor. 
  val nodeActor = actorSystem.actorFor(s"akka://bigsaas@${SocketUtils.ip}:$nodePort/user/nodeActor")
  
  // Create the application actor
  val appActor = actorSystem.actorOf(Props(new BigSaasApplicationActor(nodeActor)), "appActor")

  def activeApp = ActiveApplication(applicationId, ApplicationConfig.name, ApplicationConfig.version, appActor, DateTime.now, ActiveApplicationStatus.GREEN, Some("All ok"))
  
  import actorSystem.dispatcher
  // Periodically send a sign of life to the node.
  actorSystem.scheduler.schedule(0 seconds, ApplicationConfig.lastSignOfLifeSec seconds) {
    nodeActor ! activeApp
  }
  
  def version = 1
  def httpPort = 80
  
  def elasticSearchClient(tenant : Tenant) : ESClient = new ESClient(null.asInstanceOf[Client]) 

  case object ApplicationInfoRequest
  case class ApplicationInfoResponse(platformVersion : Int, httpPort : Option[Int], httpsPort : Option[Int])
  
  lazy val admin = new BigSaasAdminClientImpl
  
  def signOfLifeTimeout = 
    Duration(ApplicationConfig.lastSignOfLifeSec, TimeUnit.SECONDS)
    
  def signOfLife(status : ActiveApplicationStatus.Value, message : Option[String] = None) = 
    nodeActor ! activeApp
    
  def onShutdown(f : => Unit) {}
}