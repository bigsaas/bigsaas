package org.bigsaas.core.node

import org.bigsaas.core.BigSaasClient

/**
 * A node in the BigSaas cluster.
 */
class BigSaasNode {
  val elasticSearchNode = new ElasticSearchNode
  def client = new BigSaasNodeClient(this)
}