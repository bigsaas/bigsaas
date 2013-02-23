package org.bigsaas.core

class BigSaasException(message : String, cause : Throwable = null) extends Exception(message, cause) {
  def message = getMessage
}
/**
 * Thrown when a specified id was not valid. The message should be descriptive, contain the , english and is meant for end-user
 * consumption.
 */
class InvalidConfigrationException(val key : String, val value : String) extends BigSaasException(s"Invalid configuration value for key $key: $value")

/**
 */
class NoPortAvailableException(message : String) extends BigSaasException(message)

class NodeAlreadyRunningException(message : String) extends BigSaasException(message)
