package org.bigsaas.elasticsearch.server

import org.elasticsearch.node.NodeBuilder._
import org.bigsaas.elasticsearch.client.ElasticSearchClient

object ElasticSearchNode extends App {
 val node = new ElasticSearchNode
 println(node.node.settings.getAsMap)
}

class ElasticSearchNode {

  private lazy val node = nodeBuilder.node
  
  lazy val client = new ElasticSearchClient(node.client)
  
  def close = node.close
  
}