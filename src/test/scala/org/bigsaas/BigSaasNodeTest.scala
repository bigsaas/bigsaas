package org.bigsaas

import org.scalatest.FlatSpec
import org.bigsaas.util.elasticsearch.ESTest
import org.bigsaas.node.BigSaasNodeActor
import org.bigsaas.core.Tenant
import org.bigsaas.client.BigSaasClient
import org.bigsaas.util.model.Id
import org.bigsaas.client.impl.BigSaasApp

class BigSaasNodeTest extends FlatSpec with ESTest {

  val node1 = new BigSaasNodeActor
}

object NodeTest extends BigSaasApp with ESTest {
  val node1 = new BigSaasNodeActor
  val node2 = new BigSaasNodeActor
  
  val esClient = client.elasticSearchClient(Tenant(Id.generate, "ruud"))
  val index = testIndex("Persons")
}