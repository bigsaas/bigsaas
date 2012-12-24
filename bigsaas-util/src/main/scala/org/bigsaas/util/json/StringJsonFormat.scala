package org.bigsaas.util.json

import spray.json.JsonFormat
import spray.json.JsValue
import spray.json.JsString
import spray.json.deserializationError

class StringJsonFormat[A](type_ : String, f: String => A) extends JsonFormat[A] {
  def write(value: A) = JsString(value.toString)
  def read(value: JsValue): A = value match {
    case JsString(s) =>
      try f(s)
      catch {
        case e: NoSuchElementException if e.getMessage == "None.get" => deserializationError("Couldn't find a " + type_ + " for value '" + s + "'")
        case e: Throwable => deserializationError("Couldn't convert '" + s + "' to a " + type_ + ": " + e.getMessage)
      }
    case x => deserializationError("Expected " + type_ + " as String, but got " + x)
  }
}
