package twocents.api.v1

import grizzled.slf4j.Logging
import spray.http.HttpHeaders.RawHeader
import spray.http.StatusCodes
import spray.routing.Directive.pimpApply
import spray.routing.directives.CompletionMagnet.fromObject
import twocents.api.ApiDirectives
import twocents.api.ApiJsonFormats
import twocents.logic.Authentication
import twocents.logic.AuthenticationToken
import twocents.domain.model.User

case class AuthenticationUser(password: String)

object AuthenticationApiV1 {
  val tokenKey = "Auth-Token"
}

/**
 * Handling authentication before using API.
 */
trait AuthenticationApiV1 extends ApiDirectives with Logging with ApiJsonFormats {

  val authenticationApiV1 =
    path("users" / PathElement / "authentication") { userName =>
      // Check if user is authenticated.
      get {
        headerValueByName(AuthenticationApiV1.tokenKey) { tokenValue =>
          Authentication.isAuthenticated(new AuthenticationToken(tokenValue)) match {
            case Some(user) => {
              val message = "User %s is logged in".format(userName)
              trace(message)
              complete {
                Map("message" -> message)
              }
            }
            case None => {
              val message = "User %s is not logged in".format(userName)
              trace(message)
              respondWithStatus(StatusCodes.NotFound) {
                complete {
                  Map("message" -> message)
                }
              }
            }
          }
        }
      } ~
        // Log in a specific user. Password will be in the body, in json format.  
        post {
          entity(as[AuthenticationUser]) { authenticationUser =>
            Authentication.authenticate(userName, authenticationUser.password) match {
              case Some(AuthenticationToken(token)) => {
                val message = "User %s has been succesfully logged in".format(userName)
                trace(message)
                respondWithHeader(RawHeader(AuthenticationApiV1.tokenKey, token)) {
                  complete {
                    // Return authenticated user.
                    Authentication.isAuthenticated(AuthenticationToken(token)).map(user =>
                      User(user.loginName))
                  }
                }
              }
              case None => {
                trace("Tried to log in user %s but received 'Invalid userName or password.'".format(userName))
                respondWithStatus(StatusCodes.NotFound) {
                  complete {
                    Map("message" -> "Invalid userName or password.")
                  }
                }
              }
            }
          }
        } ~
        // Log out a specific user.
        delete {
          headerValueByName(AuthenticationApiV1.tokenKey) { tokenValue =>
            Authentication.isAuthenticated(new AuthenticationToken(tokenValue)) match {
              case Some(user) => {
                Authentication.deAuthenticate(AuthenticationToken(tokenValue))
                val message = "User %s has been succesfully logged out".format(userName)
                trace(message)
                complete {
                  Map("message" -> message)
                }
              }
              case None => {
                val message = "User %s is not logged in".format(userName)
                trace(message)
                respondWithStatus(StatusCodes.NotFound) {
                  complete {
                    Map("message" -> message)
                  }
                }
              }
            }
          }
        }
    }
}