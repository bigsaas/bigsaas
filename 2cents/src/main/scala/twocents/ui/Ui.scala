package twocents.ui

import scala.concurrent.duration.Duration
import spray.routing.HttpService
import spray.routing.authentication.BasicAuth
import spray.routing.directives.CachingDirectives._
import spray.httpx.encoding._
import spray.routing._
import spray.can.server.SprayCanHttpServerApp
import akka.actor.ActorRefFactory
import java.io.File
import spray.routing.directives.PathMatcher

/**
 * Define routes for swagger documentation.
 */
trait Ui extends HttpService {

  val ui = {
    path("css" / Rest) { path =>
      getFromResource("public/css/" + path)
    } ~
    path("js" / Rest) { path =>
      getFromResource("public/js/" + path)
    } ~
    path("lib" / Rest) { path =>
      getFromResource("public/lib/" + path)
    } ~
    path("img" / Rest) { path =>
      getFromResource("public/img/" + path)
    } ~ 
    path("partials" / Rest) { path =>
      getFromResource("public/partials/" + path)
    } ~
    getFromResource("public/index.html")
  }
}