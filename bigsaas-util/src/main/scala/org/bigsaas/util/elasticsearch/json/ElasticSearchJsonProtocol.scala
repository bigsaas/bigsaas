package org.bigsaas.util.elasticsearch.json

import scala.collection.JavaConversions._

import spray.json.JsArray
import spray.json.JsBoolean
import spray.json.JsNull
import spray.json.JsNumber
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue

class ElasticSearchJsonProtocol {

  def toJson(value : Any) : JsValue = value match {
    case null => JsNull
    case s : String => JsString(s)
    case n : Int => JsNumber((n))
    case n : Long => JsNumber(BigDecimal(n))
    case n : Float => JsNumber(BigDecimal(n))
    case n : Double => JsNumber(BigDecimal(n))
    case n : BigDecimal => JsNumber(n)
    case b : Boolean => JsBoolean(b)
    case c : java.util.Collection[_] => new JsArray(c.toList.map(toJson _))
    case m : java.util.Map[_, _] => toJson(m.asInstanceOf[Map[String, Any]])
    case _ => JsString("")
  }
  
  implicit def toJson(map : Map[String, Any]) : JsObject = JsObject {
    for ((field, value) <- map.toList)
      yield field -> toJson(value)
  }
}