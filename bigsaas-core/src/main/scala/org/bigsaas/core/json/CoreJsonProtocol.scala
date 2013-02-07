package org.bigsaas.core.json

import java.util.Locale

import org.bigsaas.core.model.ByLocale
import org.bigsaas.core.model.ByLocale.toMap
import org.bigsaas.core.model.Id
import org.bigsaas.util.json.StringJsonFormat

import spray.json.DefaultJsonProtocol
import spray.json.JsArray
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsonFormat
import spray.json.pimpAny

trait CoreJsonProtocol extends DefaultJsonProtocol {
  
  implicit def idFormat[A] = new StringJsonFormat[Id[A]]("Id", Id[A] _)

  implicit val localeJsonFormat = new JsonFormat[Locale] {
    def write(locale: Locale) =
      write(locale, None)

    def write(locale: Locale, additionalValue: Option[(String, JsValue)] = None) = {
      val country = locale.getCountry match {
        case "" => List.empty
        case country => List("country" -> JsString(country))
      }
      val language = locale.getLanguage match {
        case "" => List.empty
        case language => List("language" -> JsString(language))
      }
      val variant = locale.getVariant match {
        case "" => List.empty
        case variant => List("variant" -> JsString(variant))
      }
      val value = additionalValue match {
        case None => List.empty
        case Some(value) => List(value)
      }
      JsObject(country ++ language ++ variant ++ value)
    }
    def read(value: JsValue): Locale = Locale.ENGLISH
  }

  implicit def byLocaleJsonFormat[T: JsonFormat] = new JsonFormat[ByLocale[T]] {
    def write(map: ByLocale[T]) = JsArray {
      for ((locale, value) <- map.toList)
        yield localeJsonFormat.write(locale, Some("value" -> value.toJson))
    }
    def read(value: JsValue) = value match {
      case JsString(s) => ByLocale.empty
      case _ => ByLocale.empty
    }
  }

  implicit def localeMapJsonFormat[T: JsonFormat] = new JsonFormat[Map[Locale, T]] {
    def write(map: Map[Locale, T]) = map.toList match {
      case Nil =>
        JsArray()
      case (locale, value) :: Nil =>
        if (locale.getCountry.isEmpty && locale.getLanguage.isEmpty && locale.getVariant.isEmpty)
          value.toJson
        else
          localeJsonFormat.write(locale, Some("value2", value.toJson))
      case entries =>
        JsArray(for ((locale, value) <- entries)
          yield localeJsonFormat.write(locale, Some("value2", value.toJson)))
    }
    def read(value: JsValue) = value match {
      case JsString(s) => Map.empty
      case _ => Map.empty
    }
  }
}

package object implicits extends CoreJsonProtocol