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
import akka.actor.Actor
import akka.actor.ActorRef
import org.mashupbots.socko.handlers.StaticResourceRequest
import org.mashupbots.socko.routes.PathSegments

class AdminApi(actor : ActorRef, staticContentActor: ActorRef) {

  val routes = Routes {
  case HttpRequest(httpRequest) => httpRequest match {
      case GET(Path("/v1/html")) => {
        // Return HTML page to establish web socket
        staticContentActor ! httpRequest
      }
      case Path("/favicon.ico") => {
        // If favicon.ico, just return a 404 because we don't have that file
//        httpRequest.response.write(HttpResponseStatus.NOT_FOUND)
      }
      case Path(s) => {
         staticContentActor ! new StaticResourceRequest(httpRequest, "admin/public/" + s)
      }
    }

    case WebSocketHandshake(wsHandshake) => wsHandshake match {
      case Path("/v1/websocket/") => {
        // To start Web Socket processing, we first have to authorize the handshake.
        // This is a security measure to make sure that web sockets can only be established at your specified end points.
        wsHandshake.authorize("nodes")
      }
    }

    case WebSocketFrame(wsFrame) => {
      // Once handshaking has taken place, we can now process frames sent from the client
      actor ! wsFrame
    }
    
    case s => println("Some requeust: " + s)

  }
}