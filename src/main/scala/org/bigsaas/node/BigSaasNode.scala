package org.bigsaas.node

import org.bigsaas.core.NodeAlreadyRunningException
import org.bigsaas.util.Logging
import org.bigsaas.util.SocketUtils
import akka.actor.Props
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.bigsaas.util.Config
import org.bigsaas.node.admin.AdminActor
import org.mashupbots.socko.webserver.WebServer
import org.bigsaas.node.admin.AdminServer

object BigSaasNode extends Logging {

  val staticContentDispatcher = "static-content-dispatcher"
  val adminDispatcher = "admin-dispatcher"

  lazy val isAlreadyRunning = !SocketUtils.portAvailable(NodeConfig.port)
  lazy val actorSystem = ActorSystem("bigsaas",
    Config.merge(s"""
      akka.remote.transport = "akka.remote.netty.NettyRemoteTransport"
      akka.remote.netty.hostname = "${SocketUtils.ip}"
      akka.remote.netty.port = ${NodeConfig.port}
      akka.actor.provider = "akka.remote.RemoteActorRefProvider"

      $staticContentDispatcher {
        type = Dispatcher
        executor = "fork-join-executor"
        fork-join-executor.parallelism-min = 2
        fork-join-executor.parallelism-factor = 2.0
        fork-join-executor.parallelism-max = 10
      }
      
      $adminDispatcher {
        type = Dispatcher
        executor = thread-pool-executor
      }
   """))

  lazy val nodeActor = actorSystem.actorOf(Props[BigSaasNodeActor], "nodeActor")

  private lazy val adminServer = new AdminServer(actorSystem)

  private lazy val shutdownHook = new Thread {
    override def run {
      adminServer.stop
      actorSystem.shutdown
    }
  }
  
  def start {
    if (isAlreadyRunning) {
      throw new NodeAlreadyRunningException(s"There is already a BigSaas node running on port ${NodeConfig.port} (${NodeConfig.name})")
    }
    
    Logging.setLevel("bigsaas.loglevel", "org.bigsaas")
    Runtime.getRuntime.addShutdownHook(shutdownHook)
    adminServer.start
    nodeActor
  }
  def startIfNotRunning = {
    if (!isAlreadyRunning) {
      start
    }
  }
}