package org.bigsaas.client

import org.bigsaas.client.elasticsearch.ESClient
import org.bigsaas.core.Tenant
import org.bigsaas.client.admin.BigSaasAdminClient
import org.bigsaas.client.impl.BigSaasClientImpl

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
}

object BigSaasClient extends BigSaasClientImpl
