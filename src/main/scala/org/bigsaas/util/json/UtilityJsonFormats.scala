package org.bigsaas.util.json

import spray.json.deserializationError
import spray.json.JsonFormat
import spray.json.JsString
import spray.json.JsValue
import scala.concurrent.duration.Deadline
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

trait UtilityJsonFormats {

  def enumerationFormat[E <: Enumeration](enum : E) = new JsonFormat[E#Value] {
    def write(value: E#Value) = JsString(value.toString)
    def read(value: JsValue): E#Value = value match {
      case JsString(s) =>
        try enum.withName(s).asInstanceOf[E#Value]
        catch {
          case e: Throwable => deserializationError(s"Expected any of ${enum.values.mkString(",")}, but got " + s)
        }
      case x => deserializationError(s"Expected any of ${enum.values.mkString(",")}, but got " + x)
    }
  }
  
  def stringJsonFormat[A](type_ : String, f: String => A) = new JsonFormat[A] {
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
  
  implicit val dateTimeFormat = new JsonFormat[DateTime] {
    val format = ISODateTimeFormat.dateTime.withZoneUTC
    def write(value: DateTime) = JsString(format.print(value))
    def read(value: JsValue): DateTime = value match {
      case JsString(s) =>
        try format.parseDateTime(s)
        catch {
          case e: Throwable => deserializationError("Couldn't convert '" + s + "' to a date-time: " + e.getMessage)
        }
      case s => deserializationError("Couldn't convert '" + s + "' to a date-time")
    }
  }
}
