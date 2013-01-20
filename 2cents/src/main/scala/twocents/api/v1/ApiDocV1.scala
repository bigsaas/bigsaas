package twocents.api.v1

import twocents.api.ApiDirectives
import twocents.api.ApiJsonFormats

import spray.http.StatusCodes
import spray.routing.Directive.pimpApply
import spray.routing.HttpService

/**
 * Define routes for swagger documentation.
 */
trait ApiDocV1 extends HttpService with ApiJsonFormats with ApiDirectives {

  val apiDocV1 = {
    path("api.html" / Rest) { path =>
      getFromResource("public/html/restApiDoc.html")
    } ~ 
      path("css" / Rest) { path =>
        getFromResource("public/css/" + path)
      } ~
      path("js" / Rest) { path =>
        getFromResource("public/js/" + path)
      } ~
      path("images" / Rest) { path =>
        getFromResource("public/images/" + path)
      } ~ 
      path("json" / Rest) { path =>
        getFromResource("public/json/" + path)
      } 
  }
}