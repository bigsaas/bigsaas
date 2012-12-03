package org.bigsaas

import org.scalatest.FlatSpec
import org.bigsaas.elasticsearch.server.ElasticSearchNode
import org.bigsaas.domain.catalog.CatalogItem
import org.bigsaas.domain.catalog.CatalogJsonMapping._
import play.api.libs.json.Json
import org.bigsaas.core.model.Id
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import org.bigsaas.core.Tenant
import org.bigsaas.domain.party.Party

class ElasticSearchTest extends FlatSpec {

  "A select statement" should "contain select string" in {
    implicit val tenant = new Tenant
    val node = new ElasticSearchNode
    val client = node.client
    val datastore = client.datastore("assets", "assets")
    val party = Party(name="me")
    
    val asset = CatalogItem(Id("12"), owner=party.id)
//    val result = datastore.store(asset)
//    result.onComplete {
//      case Success(value) =>
//        println("id: " + value)
//        datastore.get(Id[Asset]("12")).onComplete {
//          case Success(value) => println("value: " + value)
//          case Failure(e) => println("exception: " + e)
//        }
//      case Failure(e) => println("exception: " + e)
//    }
    Thread.sleep(5000)
  }
}