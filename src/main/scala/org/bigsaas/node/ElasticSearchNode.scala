package org.bigsaas.node

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.node.NodeBuilder
import grizzled.slf4j.Logging
import org.scalastuff.esclient.ESClient
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest
import org.bigsaas.core.BigSaasConfig

class ElasticSearchNode extends Logging {

  val clusterName = "bigsaas"
  val dataNode = BigSaasConfig.dataNodes.contains(BigSaasConfig.nodeName)
    
  if (dataNode) info("Starting ElasticSearch data node")
  else info("Starting ElasticSearch node")
  
  val settings = ImmutableSettings.settingsBuilder.
    put("org.elastcisearch.discovery", "TRACE").
    put("node.client", "false").
    put("node.data", dataNode).build
  
  private val node = NodeBuilder.nodeBuilder.settings(settings).clusterName(clusterName).node
  private val client = node.client

  val esClient = new ESClient(node.client)
  val nodes : NodesInfoResponse = Await.result(esClient.execute(new NodesInfoRequest), Duration.Inf)
  for (node <- nodes.nodes) {
  }
  println("Nodes: " + nodes.nodes.toSeq.map(_.getNode().address()))
  // on shutdown

  def shutdown {
    node.close();
  }

}