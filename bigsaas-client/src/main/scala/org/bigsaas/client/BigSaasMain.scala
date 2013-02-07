package org.bigsaas.client

trait BigSaasMain {

  def start(client : BigSaasClient, httpPort : Option[Int], httpsPort : Option[Int])
}