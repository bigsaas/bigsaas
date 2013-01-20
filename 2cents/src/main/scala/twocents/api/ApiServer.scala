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
import spray.http.HttpRequest
import spray.routing.directives.LogEntry
import spray.http.HttpResponse
import spray.http.HttpBody
import spray.routing.Rejected
import akka.event.Logging.WarningLevel

/*
 * Actor for Ui project.
 */
class ApiActor extends Actor with HttpServiceActor with Api {
  override def receive = runRoute{
    logRequestResponse(routeLog _) {
      api
    }
  }
  
  def routeLog(request: HttpRequest): Any => Option[LogEntry] = {
    case x: HttpResponse => {
      x.entity match {
        case e: HttpBody => {
          if (e.contentType.mediaType.binary) {
            Some(LogEntry(request + ":" + x.status + " " + e.contentType, WarningLevel))
          }
          else if (e.contentType.mediaType.subType.contains("json")) {
            Some(LogEntry(request+ ":" +  x.toString(), WarningLevel))
          }
          else {
            Some(LogEntry(request+ ":" + x.status + " " + e.contentType, WarningLevel))
          }
        }
        case _ => Some(LogEntry(request+ ":" +  x.toString()))
      }
    } // log response
    case Rejected(rejections) => 
      Some(LogEntry(request+ ":" +   " Rejection " + rejections.toString(), WarningLevel))
    case x => Some(LogEntry(request+ ":" +   x.toString(), WarningLevel))
  }
}

/**
 * Create a server for API project using spray-can.
 */
object ApiServer extends App with SprayCanHttpServerApp {

  val service = system.actorOf(Props[ApiActor], "asset-service")

  newHttpServer(service) ! Bind(interface = "localhost", port = 9000)
}
