package org.bigsaas.core.model

import play.api.libs.json._
import java.util.Locale
import scala.Enumeration

package object json {

  implicit def idReads[A] = Reads[Id[A]] { jsValue => JsSuccess(new Id[A](jsValue.toString)) }
  implicit def idWrites[A] = Writes[Id[A]] { id => JsString(id.id) }

  implicit val localeReads = Reads[Locale] { jsValue => JsSuccess(new Locale(jsValue.toString)) }
  implicit val localeWrites = Writes[Locale] { locale => JsString(locale.toString) }
  
  implicit def mapReads[A, B] = Reads[Map[A, B]] { jsValue => JsSuccess(null) }
  
}