package org.bigsaas.domain.catalog

import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.libs.json.util._

import org.bigsaas.core.model.CoreJsonMapping._
import org.bigsaas.core.model.Id


object CatalogJsonMapping {

//  implicit val catalogItemReads = (
//    (__ \ "id").read[Id[CatalogItem]] and
//    (__ \ "owner").read[Id[Party]] and
//    (__ \ "name").read[String] and
//    (__ \ "displayName").read[Map[Locale, String]])(CatalogItem)
//
//  implicit val catalogItemWrites = (
//    (__ \ "id").write[Id[CatalogItem]] and
//    (__ \ "owner").write[Id[Party]] and
//    (__ \ "name").write[String] and
//    (__ \ "displayName").write[Map[Locale, String]])(unlift(CatalogItem.unapply))


  implicit val propertyWrites : Writes[Property] = (
    (__ \ "id").write[Id[Property]] and
    (__ \ "name").write[String] and
    (__ \ "displayName").write[Map[Locale, String]] and
    (__ \ "type").write[PropertyType.Value] and
    (__ \ "scope").write[PropertyScope.Value])(unlift(Property.unapply))

}