package org.bigsaas.util.elasticsearch

import org.scalatest.FlatSpec
import org.elasticsearch.action.get.GetRequest
import scala.concurrent.Future
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.get.GetAction
import org.elasticsearch.action.get.GetRequestBuilder
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse
import org.scalastuff.esclient.ESClient

class ESClientTest extends FlatSpec with ESTest {
  
  val result : Future[GetResponse] = {
      val f : Future[GetResponse] = es.execute(new GetRequest("index"))
      val f2 : Future[ClusterHealthResponse] = es.execute(new ClusterHealthRequest("index"))
      es.execute(new GetRequest("index"))
  }
}