package twocents.api

import akka.actor.Actor
import akka.actor.Props
import spray.can.server.SprayCanHttpServerApp
import spray.http.StatusCodes.BadRequest
import spray.http.StatusCodes.InternalServerError
import spray.http.StatusCodes.Unauthorized
import spray.routing.ExceptionHandler
import spray.routing.HttpServiceActor
import spray.util.LoggingContext
import spray.routing.RequestContext
import spray.routing.RejectionHandler
import spray.routing.RoutingSettings

/*
 * Actor for Ui project.
 */
class ApiActor extends Actor with HttpServiceActor with Api {
  def receive = runRoute(api)
}

/**
 * Create a server for API project using spray-can.
 */
object ApiServer extends App with SprayCanHttpServerApp {

  val service = system.actorOf(Props[ApiActor], "asset-service")

  newHttpServer(service) ! Bind(interface = "localhost", port = 9000)
}
