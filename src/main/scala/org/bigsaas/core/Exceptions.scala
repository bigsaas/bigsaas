package org.bigsaas.core

class BigSaasException(message : String, cause : Throwable = null) extends Exception(message, cause) {
  def message = getMessage
}

/**
 */
class NoPortAvailableException(message : String) extends BigSaasException(message)

class PortAlreadyInUseException(message : String) extends BigSaasException(message)

class NodeAlreadyRunningException(message : String) extends BigSaasException(message)
