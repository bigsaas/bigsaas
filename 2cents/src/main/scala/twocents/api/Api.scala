package twocents.api

import akka.event.Logging.InfoLevel
import grizzled.slf4j.Logging
import spray.http.HttpRequest
import spray.http.StatusCodes
import spray.routing.Directive.pimpApply
import spray.routing.ExceptionHandler
import spray.routing.directives.LogEntry
import twocents.api.v1.ApiDocV1
import twocents.api.v1.DocumentsApiV1
import twocents.commons.InvalidIdException
import twocents.commons.InvalidInputException
import twocents.commons.NotAuthorizedException

trait Api extends DocumentsApiV1 with ApiDocV1 with Logging {

  val api =
    pathPrefix("v1") {
      logRequest(showRequest _) {
        documentsApiV1 ~
          apiDocV1
      }
    }

  def showRequest(request: HttpRequest) = LogEntry("URL: " + request.uri + "\n CONTENT: " + request.entity, InfoLevel)
  
  implicit val exceptionHandler = ExceptionHandler.fromPF {
    case e: NotAuthorizedException => ctx =>
      warn(s"Unauthorized request ${ctx.request} : ${e.message}", e)
      ctx.complete(StatusCodes.Unauthorized, Map("message" -> e.message))
    case e: InvalidIdException => ctx =>
      trace(s"Request ${ctx.request} contains invalid input: ${e.message}", e)
      ctx.complete(StatusCodes.BadRequest, Map("message" -> e.message, "id" -> e.id, "type" -> e.`type`))
    case e: InvalidInputException => ctx =>
      trace(s"Request ${ctx.request} contains invalid input: ${e.message}", e)
      ctx.complete(StatusCodes.BadRequest, Map("message" -> e.message))
    case e: Throwable => ctx =>
      error(s"Request ${ctx.request} could not be handled normally: ${e.getMessage}", e)
      ctx.complete(StatusCodes.InternalServerError, Map("message" -> e.getMessage))
  }
}

