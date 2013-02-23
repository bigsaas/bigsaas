package org.bigsaas.util.elasticsearch

import collection.mutable
import org.elasticsearch.node.NodeBuilder
import org.elasticsearch.client.Client
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.indices.IndexMissingException
import org.elasticsearch.action.count.CountRequest
import org.bigsaas.util.concurrent.waitFor

import scala.concurrent.Await
import scala.concurrent.duration._
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.scalastuff.esclient.ESClient

object ESTest {
  private def startClient = {
    val node = NodeBuilder.nodeBuilder.clusterName("Test").data(true).build
    node.start
    node.client
  } 
}

/**
 * To be used by unit tests that use elastic search.
 */
trait ESTest {
  
  private val indexes = mutable.Map[String, String]()
  
  def testIndex(name : String) = {
    val fullName = (getClass.getSimpleName + "_" + name).toLowerCase
    indexes.getOrElseUpdate(fullName, { 
      try {
        ESClient(es).execute(new DeleteIndexRequest(fullName))
          es.admin.indices.delete(new DeleteIndexRequest(fullName)).actionGet
      } catch {
        case e: IndexMissingException => // Ok, might be the first time
      }
      es.admin.indices.create(new CreateIndexRequest(fullName)).actionGet
      fullName
    })
  }
  
  implicit protected lazy val es = ESTest.startClient

}