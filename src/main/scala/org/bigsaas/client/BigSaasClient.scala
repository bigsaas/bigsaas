package org.bigsaas.client

import org.bigsaas.client.elasticsearch.ESClient
import org.bigsaas.core.Tenant
import org.bigsaas.client.admin.BigSaasAdminClient
import org.bigsaas.client.impl.BigSaasClientImpl
import org.bigsaas.core.ActiveApplicationStatus
import scala.concurrent.duration.Duration
import akka.actor.ActorRef

/**
 * Remote interface to the BigSaas platform.
 */
trait BigSaasClient {
  
  /**
   * Current platform version.
   */
  def version : Integer
  
  /**
   * Current http port assigned by the platform.
   */
  def httpPort : Int
  
  /**
   * Gets an elastic search client for given tenant.
   */
  def elasticSearchClient(tenant : Tenant) : ESClient
  
  def admin : BigSaasAdminClient
  
  def signOfLifeTimeout : Duration
  
  def nodeActor : ActorRef
  
  /**
   * Send a message to the bigsaas platform that the application is still running.
   */
  def signOfLife(status : ActiveApplicationStatus.Value, message : Option[String] = None)
}
