package org.bigsaas.core

import org.bigsaas.util.elasticsearch.ESClient

/**
 * Interface to the BigSaas platform.
 */
trait BigSaasClient {
  
  /**
   * Current platform version.
   */
  def version : Integer
  
  /**
   * Gets an elastic search client for given tenant.
   */
  def elasticSearchClient(tenant : Tenant) : ESClient 
}