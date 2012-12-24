package org.bigsaas.domain.catalog.model

import java.util.Locale
import scala.concurrent.duration.Deadline
import java.text.SimpleDateFormat
import java.util.TimeZone
import org.bigsaas.core.model.HasId
import org.bigsaas.core.model.Id
import org.bigsaas.domain.party.model.Party

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
  when : Deadline = Deadline.now
) extends HasId[CatalogItem]

object CatalogItem {
  val dateTimeFormat = {
    val df = new SimpleDateFormat("dd-MM-yyyy HH:mm")
    df.setTimeZone(TimeZone.getTimeZone("UTC"))
    df
  }
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
        Some(dateTimeFormat.parse(value))
      } catch {
        case _ : Throwable => None
      }
      case _ => None
    }
  }
  def toString(value : Any, `type` : PropertyType.Value) : String = 
    `type` match {
    case PropertyType.DateTime => 
      if (value.isInstanceOf[Deadline]) {
        dateTimeFormat.format(value.asInstanceOf[Deadline].time.length)
      } else {
        value.toString
      }
    case _ => value.toString
  }
  
  def convertValue(value : Any, fromType : PropertyType.Value, toType : PropertyType.Value) : Option[Any] = 
    parseValue(toString(value, fromType), toType)
}
