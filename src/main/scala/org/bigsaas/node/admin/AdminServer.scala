package org.bigsaas.node.admin

import org.mashupbots.socko.webserver.WebServer
import org.mashupbots.socko.webserver.WebServerConfig
import akka.actor.ActorSystem
import org.mashupbots.socko.routes.Routes
import org.mashupbots.socko.routes.HttpRequest
import org.mashupbots.socko.routes.GET
import org.mashupbots.socko.routes.Path
import akka.actor.Props
import org.mashupbots.socko.events.HttpResponseStatus
import org.mashupbots.socko.routes.WebSocketHandshake
import org.mashupbots.socko.routes.WebSocketFrame
import org.mashupbots.socko.handlers.StaticContentHandlerConfig
import org.mashupbots.socko.handlers.StaticContentHandler
import akka.routing.FromConfig
import akka.dispatch.MessageDispatcher
import akka.routing.RoundRobinRouter
import org.bigsaas.node.BigSaasNode

class AdminServer(actorSystem: ActorSystem) {

  private val adminActor = actorSystem.actorOf(Props[AdminActor].
    withRouter(RoundRobinRouter(nrOfInstances = 5)).withDispatcher(BigSaasNode.adminDispatcher))

  private val staticContentActor = actorSystem.actorOf(Props(new StaticContentHandler(StaticContentHandlerConfig())).
      withRouter(RoundRobinRouter(nrOfInstances = 5)).withDispatcher(BigSaasNode.staticContentDispatcher))

  private val api = new AdminApi(adminActor, staticContentActor)
  private val webServer = new WebServer(WebServerConfig(port = AdminConfig.port), api.routes, actorSystem)

  def start = webServer.start
  def stop = webServer.stop
}