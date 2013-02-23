package org.bigsaas.client

import org.bigsaas.client.impl.BigSaasClientImpl

object ESClientTest extends App {

  val client = new BigSaasClientImpl
  println("Client started")
}