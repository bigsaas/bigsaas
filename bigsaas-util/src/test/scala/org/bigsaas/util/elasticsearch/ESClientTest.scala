package org.bigsaas.util.elasticsearch

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import org.scalatest.FlatSpec
import spray.json.DefaultJsonProtocol.StringJsonFormat
import spray.json.DefaultJsonProtocol.jsonFormat1
import spray.json.DefaultJsonProtocol.jsonFormat3
import spray.json.DefaultJsonProtocol.optionFormat
import org.bigsaas.util.json.implicits._
import spray.json.JsObject

case class Person(id: String, firstName: Option[String] = None, lastName: Option[String] = None)

class ESClientTest extends FlatSpec with ESTest {

  val index = testIndex("Persons")
  val indexType = ESType("Person")
  val person = Person("1", Some("George"), Some("Baker"))
  implicit val personFormat = jsonFormat3(Person)
  def wait[A](future: Future[A]) = Await.result(future, 10 seconds)

  "The index" should "be initially empty" in {
    intercept[Exception] {
      val p2: Person = wait(es.get[Person](index, ESTypeAll, person.id))
    }
  }

  "A typed object" should "be stored as Json" in {
    val id: String = wait(es.index(index, indexType, person.id, person))
    assert(id == person.id)
  }

  "The same typed object" should " be retrieved from ES." in {
    val retrievedPerson: Person = wait(es.get[Person](index, ESTypeAll, person.id))
    assert(person == retrievedPerson)
  }
}
