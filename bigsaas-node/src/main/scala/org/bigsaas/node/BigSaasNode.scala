package org.bigsaas.node

import org.bigsaas.client.BigSaasClient
import org.bigsaas.client.impl.BigSaasClientImpl

/**
 * A node in the BigSaas cluster.
 */
class BigSaasNode {
  val elasticSearchNode = new ElasticSearchNode
  def client = new BigSaasClientImpl("")
}