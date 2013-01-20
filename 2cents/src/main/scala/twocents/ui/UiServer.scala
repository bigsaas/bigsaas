package twocents.ui

import akka.actor.Actor
import spray.routing.HttpService
import spray.can.server.SprayCanHttpServerApp
import akka.actor.Props
import spray.routing.Directives
import twocents.api.Api
import twocents.api.ApiActor
import spray.routing.HttpServiceActor

/*
 * Actor for Ui project.
 */
class UiActor extends ApiActor with Ui {
  override def receive = runRoute{
    logRequestResponse(routeLog _) {
      api ~ ui
    }
  }
}

/**
 * Create a server for Ui project using spray-can.
 */
object UiServer extends App with SprayCanHttpServerApp {

  val service = system.actorOf(Props[UiActor], "2cents")

  newHttpServer(service) ! Bind(interface = "0.0.0.0", port = 9002)
}