package twocents.logic

import java.math.BigInteger
import java.security.SecureRandom
import java.util.concurrent.TimeUnit

import twocents.domain.model.User
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.typesafe.config.ConfigFactory

case class AuthenticationToken(token: String)

object Authentication {
  val secureRandom = new SecureRandom()
  val config = ConfigFactory.load.getConfig("machinexs.authentication")
  val expiryTimeSec = config.getLong("expiryTimeSec")

  val authenticatedUsers: Cache[AuthenticationToken, User] =
    CacheBuilder.newBuilder.expireAfterAccess(expiryTimeSec, TimeUnit.SECONDS).build[AuthenticationToken, User]

  /**
   * Logic for doing authentication of a specific user. If succeeds, returns primary key of that user.
   */
  def authenticate(username: String, password: String): Option[AuthenticationToken] = {
//    val user = Users.findByUserAndPassword(username, password)
    val user : Option[User] = Some(User("Ruud"))
    user match {
      case Some(user) => {
        val token = getSecureId
        authenticatedUsers.put(AuthenticationToken(token), user)
        println("User %s has just logged in.".format(user.loginName))
        Some(AuthenticationToken(token))
      }
      case None => None
    }
  }
  
  /**
   * Clear a specific token from cache. Used to log-out a user.
   */
  def deAuthenticate(token: AuthenticationToken) = {
      authenticatedUsers.invalidate(token)
  }

  /**
   * Check if a specific token exists in cache. Used to check if a user is logged-in.
   */
  def isAuthenticated(token: AuthenticationToken): Option[User] = {
    println("Authentication token: " + token)
    authenticatedUsers.getIfPresent(token) match {
      case null => None
      case user => Some(user)
    }
  }

  def getSecureId = {
    new BigInteger(130, secureRandom).toString(32);
  }
}