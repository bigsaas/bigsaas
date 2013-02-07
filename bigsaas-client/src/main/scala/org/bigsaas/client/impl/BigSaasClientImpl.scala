package org.bigsaas.client.impl

import org.bigsaas.client.BigSaasClient
import org.bigsaas.client.elasticsearch.ESClient
import org.bigsaas.core.Tenant
import org.elasticsearch.client.Client

class BigSaasClientImpl(startupParameters : ApplicationStartupParameters) extends BigSaasClient {

  val actorSystem = new BigSaasActorSystem(startupParameters.actorPort)
  val applicationActor = actorSystem.system.actorFor(startupParameters.applicationActorPath)
  
  
  def version = startupParameters.platformVersion
  def httpPort = startupParameters.httpPort
  
  def elasticSearchClient(tenant : Tenant) : ESClient = new ESClient(null.asInstanceOf[org.bigsaas.util.elasticsearch.ESClient]) 

  case object ApplicationInfoRequest
  case class ApplicationInfoResponse(platformVersion : Int, httpPort : Option[Int], httpsPort : Option[Int])
}