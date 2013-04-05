package org.bigsaas.client.impl

import org.bigsaas.client.BigSaasClient
import org.bigsaas.util.Logging

abstract class BigSaasApp extends App with Logging { 

  lazy val client : BigSaasClient = try {
    new BigSaasClientImpl {
      onShutdown(shutdown)
    }
  }
  catch {
    case e : Throwable => println("Error starting the application: " + e.getMessage)
    throw e
    null
  }
  
  def shutdown {}
}