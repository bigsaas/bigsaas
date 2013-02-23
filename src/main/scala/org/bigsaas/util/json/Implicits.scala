package org.bigsaas.util.json

import spray.json.JsObject
import spray.json.JsValue
import spray.json.JsonFormat

trait Implicits {
  
  implicit object JsObjectIdentityFormat extends JsonFormat[JsObject] {
    def write(jsObject: JsObject) = jsObject 
    def read(value: JsValue) = value match {
      case jsObject : JsObject => jsObject
      case value => JsObject("value" -> value)
    }
  }
}

package object implicits extends Implicits