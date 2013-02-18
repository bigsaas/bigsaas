package org.bigsaas.node

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.node.NodeBuilder
import grizzled.slf4j.Logging
import org.scalastuff.esclient.`package`.ESClient
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest

class ElasticSearchNode extends Logging {

  val clusterName = "bigsaas"
  
  info("Starting elastic search data node")
  
  val settings = ImmutableSettings.settingsBuilder.
    put("org.elastcisearch.discovery", "TRACE").
    put("node.client", "false").
    put("node.data", "false").build
  
  private val node = NodeBuilder.nodeBuilder.settings(settings).clusterName(clusterName).node
  private val client = node.client

  val esClient = new ESClient(node.client)
  val nodes : NodesInfoResponse = Await.result(esClient.execute(new NodesInfoRequest), Duration.Inf)
  println("Nodes: " + nodes.nodes.toSeq.map(_.getNode().address()))
  // on shutdown

  def shutdown {
    node.close();
  }

}