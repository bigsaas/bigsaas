package twocents.api

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import twocents.api.v1.AuthenticationApiV1
import twocents.logic.Authentication
import twocents.logic.AuthenticationToken
import twocents.domain.model.User
import spray.http.ContentType.apply
import spray.http.HttpBody
import spray.http.MediaTypes
import spray.httpx.SprayJsonSupport
import spray.routing.AuthenticationFailedRejection
import spray.routing.Directives
import spray.routing.RequestContext
import spray.routing.RouteConcatenation
import spray.routing.authentication.Authentication
import spray.routing.authentication.ContextAuthenticator
import spray.routing.directives.BasicDirectives
import spray.routing.directives.ChunkingDirectives
import spray.routing.directives.CookieDirectives
import spray.routing.directives.DebuggingDirectives
import spray.routing.directives.EncodingDirectives
import spray.routing.directives.ExecutionDirectives
import spray.routing.directives.FileAndResourceDirectives
import spray.routing.directives.FormFieldDirectives
import spray.routing.directives.HeaderDirectives
import spray.routing.directives.HostDirectives
import spray.routing.directives.MarshallingDirectives
import spray.routing.directives.MethodDirectives
import spray.routing.directives.MiscDirectives
import spray.routing.directives.ParameterDirectives
import spray.routing.directives.PathDirectives
import spray.routing.directives.RespondWithDirectives
import spray.routing.directives.RouteDirectives
import spray.json.JsonFormat
import spray.json.JsField
import spray.json.JsObject

trait ApiDirectives extends RouteConcatenation
  with BasicDirectives
  with ChunkingDirectives
  with CookieDirectives
  with DebuggingDirectives
  with EncodingDirectives
  with ExecutionDirectives
  with FileAndResourceDirectives
  with FormFieldDirectives
  with HeaderDirectives
  with HostDirectives
  with MarshallingDirectives
  with MethodDirectives
  with MiscDirectives
  with ParameterDirectives
  with PathDirectives
  with RespondWithDirectives
  with RouteDirectives
  with SprayJsonSupport {

  val myAuthenticator : ContextAuthenticator[User] = { ctx => Future(doAuthenticate(ctx)) }
  
  def doAuthenticate(ctx: RequestContext): Authentication[User] = {
//    val tokenValue = ctx.request.headers.filter(header => header.name == AuthenticationApiV1.tokenKey.toLowerCase()).headOption.map(_.value).getOrElse("")
//    Authentication.isAuthenticated(new AuthenticationToken(tokenValue)) match {
//      case Some(user) => Right(user)
//      case None => Left(AuthenticationFailedRejection("machinexs"))
//    }
    Right(User("ruud"))
  }
    
  def authenticate = Directives.authenticate(myAuthenticator)
}