package org.bigsaas

import org.scalatest.FlatSpec
import org.bigsaas.util.elasticsearch.ESTest
import org.bigsaas.node.BigSaasNodeActor
import org.bigsaas.core.Tenant
import org.bigsaas.client.BigSaasClient

class BigSaasNodeTest extends FlatSpec with ESTest {

  val node1 = new BigSaasNodeActor
}

object NodeTest extends App with ESTest {
  val node1 = new BigSaasNodeActor
  val node2 = new BigSaasNodeActor
  
  val esClient = BigSaasClient.elasticSearchClient(Tenant("ruud"))
  val index = testIndex("Persons")
}