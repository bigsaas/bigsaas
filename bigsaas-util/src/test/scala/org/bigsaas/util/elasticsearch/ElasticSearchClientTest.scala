package org.bigsaas.util.elasticsearch

import org.scalatest.FlatSpec
import org.elasticsearch.node.NodeBuilder
import org.bigsaas.util.elasticsearch.implicits._
import spray.json.DefaultJsonProtocol._
import scala.concurrent.Await
import scala.concurrent.duration._
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import scala.concurrent.Future
import org.scalatest.BeforeAndAfterAll
import org.elasticsearch.client.Client
import org.elasticsearch.indices.IndexMissingException

case class Person(id: String, firstName: Option[String] = None, lastName: Option[String] = None)

class ElasticSearchClientTest extends FlatSpec with BeforeAndAfterAll {

  val clusterName = "Test"
  val indexName = "Persons"
  val indexType = "Person"
  val person = Person("1", Some("George"), Some("Baker"))
  implicit val personFormat = jsonFormat3(Person)
  implicit def awaitFuture[A](future: Future[A]) = Await.result(future, 10 seconds)
  var client: Client = null

  override protected def beforeAll {
    val node = NodeBuilder.nodeBuilder.clusterName(clusterName).data(true).build
    node.start
    client = node.client
    try {
      client.admin.indices.delete(new DeleteIndexRequest(indexName)).actionGet
    } catch {
      case e: IndexMissingException =>
    }
  }

  "The index" should "be initially empty" in {
    intercept[Exception] {
      val p2: Person = client.getById[Person](indexName, indexType, person.id)
    }
  }

  "A typed object" should "be stored as Json" in {
    val id: String = client.store("persons", "person", person.id, person)
    assert(id == person.id)
  }

  "The same typed object" should " be retrieved from ES." in {
    val retrievedPerson: Person = client.getById[Person]("persons", "person", person.id)
    assert(person == retrievedPerson)
  }
}