package org.bigsaas.domain.catalog


import org.bigsaas.core.model.Id
import org.bigsaas.core.model.HasId
import org.bigsaas.core.model.json._
import java.util.Locale
import org.bigsaas.domain.party.Party
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Json
import play.api.libs.json._
import play.api.libs.json.util._

package object json {
  
  
  implicit val propertyScopeReads = Reads[PropertyScope.Value] { jsValue => JsSuccess(PropertyScope.withName(jsValue.toString)) }
  implicit val propertyTypeReads = Reads[PropertyType.Value] { jsValue => JsSuccess(PropertyType.withName(jsValue.toString)) }
  
  implicit val propertyReads = Json.reads[Property]
}

case class Property(
  id : Id[Property] = Id.generate,
  name : String,
  displayName : Map[Locale, String] = Map.empty,
  type_ : PropertyType.Value = PropertyType.String,
  scope : PropertyScope.Value = PropertyScope.Asset
) extends HasId[Property]

object PropertyType extends Enumeration {
  val String = Value("string")
  val Int = Value("int") 
  val Real = Value("real")
  val DateTime = Value("datetime")
}

object PropertyScope extends Enumeration {
  val Item = Value("item")
  val Asset = Value("asset") 
  val Reading = Value("reading")
}

case class PropertyValue(
  property : Id[Property],
  locale : Option[Locale] = None,
  value : Any
)

case class CatalogItem (
  id : Id[CatalogItem] = Id.generate,
  owner: Id[Party],
  name : String = "",
  displayName : Map[Locale, String] = Map.empty,
  make : Option[String] = None,
  parents : Seq[Id[CatalogItem]] = Seq.empty,
  properties : Seq[Property] = Seq.empty,
  values : Seq[PropertyValue] = Seq.empty,
  when : DateTime = new DateTime(DateTimeZone.UTC)
) extends HasId[CatalogItem]

object CatalogItem {
  val dateTimeFormat = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
  def parseValue(value : String, `type` : PropertyType.Value) : Option[Any] = {
    `type` match {
      case PropertyType.String => Some(value)
      case PropertyType.Int => try {
        Some(java.lang.Long.parseLong(value))
      } catch {
        case _ : java.lang.NumberFormatException => None
      }
      case PropertyType.Real => try {
        Some(java.lang.Double.parseDouble(value))
      } catch {
        case _ : java.lang.NumberFormatException => None
      }
      case PropertyType.DateTime => try {
        Some(dateTimeFormat.parseDateTime(value))
      } catch {
        case _ : Throwable => None
      }
      case _ => None
    }
  }
  def toString(value : Any, `type` : PropertyType.Value) : String = 
    `type` match {
    case PropertyType.DateTime => 
      if (value.isInstanceOf[DateTime]) {
        dateTimeFormat.print(value.asInstanceOf[DateTime])
      } else {
        value.toString
      }
    case _ => value.toString
  }
  
  def convertValue(value : Any, fromType : PropertyType.Value, toType : PropertyType.Value) : Option[Any] = 
    parseValue(toString(value, fromType), toType)
}
