package org.bigsaas

import org.scalatest.FlatSpec
import org.bigsaas.node.BigSaasNode
import org.bigsaas.core.Tenant
import org.bigsaas.core.TenantIsolation
import org.bigsaas.util.elasticsearch.ESTest
import java.util.UUID

class BigSaasNodeTest extends FlatSpec with ESTest {

  val node1 = new BigSaasNode
}

object NodeTest extends App with ESTest {
  val node1 = new BigSaasNode
  val node2 = new BigSaasNode
  
  val esClient = node2.client.elasticSearchClient(Tenant("ruud"))
  val index = testIndex("Persons")
}