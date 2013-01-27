package org.bigsaas.client.elasticsearch

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.scalatest.FlatSpec
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.DefaultJsonProtocol.jsonFormat1
import spray.json.DefaultJsonProtocol.jsonFormat3
import spray.json.DefaultJsonProtocol.optionFormat
import org.bigsaas.core.json.implicits._
import spray.json.JsObject
import org.bigsaas.util.elasticsearch.ESTest
import org.bigsaas.core.model.Id

case class Person(id: Id[Person], firstName: Option[String] = None, lastName: Option[String] = None)

class ESClientTest extends FlatSpec with ESTest {

  val index = ESIndex(testIndex("Persons"))
  val indexType = ESType("Person")
  val person = Person(Id("1"), Some("George"), Some("Baker"))
  implicit val personFormat = jsonFormat3(Person)
  def wait[A](future: Future[A]) = Await.result(future, 10 seconds)
  val es2 = new ESClient(es)

  "The index" should "be initially empty" in {
    intercept[Exception] {
      val p2: Person = wait(es2.get(index, ESTypeAll, person.id))
    }
  }

  "A typed object" should "be stored as Json" in {
    val id: String = wait(es2.index(index, indexType, person.id, person)).id
    assert(id == person.id)
  }

  "The same typed object" should " be retrieved from ES." in {
    val retrievedPerson: Person = wait(es2.get(index, ESTypeAll, person.id))
    assert(person == retrievedPerson)
  }
}
