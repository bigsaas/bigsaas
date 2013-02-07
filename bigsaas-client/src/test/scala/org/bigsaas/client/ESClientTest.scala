package org.bigsaas.client

import org.bigsaas.client.impl.BigSaasClientImpl
import org.bigsaas.client.impl.ApplicationStartupParameters

object ESClientTest extends App {

  val parameters = ApplicationStartupParameters(1, 8080, 8081, 2556, "sadf")
  val client = new BigSaasClientImpl(parameters)
  println("Client started")
}