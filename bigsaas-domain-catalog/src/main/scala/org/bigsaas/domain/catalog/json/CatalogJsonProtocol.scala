package org.bigsaas.domain.catalog.json

import org.bigsaas.core.implicits._
import org.bigsaas.core.model.Id
import org.bigsaas.core.model.ByLocale
import org.bigsaas.core.json.implicits._
import spray.json.JsonFormat
import spray.json.JsString
import spray.json.JsValue
import spray.json.deserializationError
import spray.json._
import java.util.Locale
import org.bigsaas.util.json.StringJsonFormat
import org.bigsaas.domain.catalog.model.PropertyType
import org.bigsaas.domain.catalog.model.PropertyScope

trait CatalogJsonProtocol {

  implicit object PropertyTypeFormat extends StringJsonFormat[PropertyType.Value]("PropertyType", PropertyType.withName _)
  implicit object PropertyScopeFormat extends StringJsonFormat[PropertyScope.Value]("PropertyScope", PropertyScope.withName _)
  
  implicit val x = jsonFormat5(Person)
  
//  implicit val propertyFormat = lazyFormat(jsonFormat1(String, "", 0))
}

package object implicits extends CatalogJsonProtocol 

case class Person(id : Id[Person] = Id.generate, firstName : Option[String] = None, lastName : Option[String] = None, scope : PropertyScope.Value,
    displayName : ByLocale[String])

object Test extends App {
	import implicits._
  
  val p = Person(firstName="Ruud", scope = PropertyScope.Asset, displayName = Map(Locale.CANADA_FRENCH -> "Le Ruud", Locale.ENGLISH -> "Rude"))
	  println(p)
  println(p.copy(displayName = p.displayName ++ Map(Locale.JAPAN-> "RUDU")))
  val j = p.toJson
  println(j)
  val p2 = j.convertTo[Person]
  println(p2)
  println("""{"id":"234", "scope":"asset", "displayName":"[]"}""".asJson.convertTo[Person])
  
}