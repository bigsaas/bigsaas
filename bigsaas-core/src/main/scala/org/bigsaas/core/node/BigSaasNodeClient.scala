package org.bigsaas.core.node

import org.bigsaas.core.BigSaasClient
import org.bigsaas.util.elasticsearch.ESClient
import org.bigsaas.core.Tenant

class BigSaasNodeClient(node : BigSaasNode) extends BigSaasClient {
  
 def version : Integer = 1
  
  /**
   * Gets an elastic search client for given tenant.
   */
  def elasticSearchClient(tenant : Tenant) : ESClient = null
  
}