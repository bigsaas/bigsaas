package org.bigsaas.domain.assetmanagement

import org.bigsaas.core.model.HasId
import org.bigsaas.core.model.Id
import java.util.Locale
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.bigsaas.domain.party.Party
import org.bigsaas.domain.catalog.CatalogItem
import org.bigsaas.domain.catalog.PropertyValue

case class Asset(
  id : Id[Asset] = Id.generate,
  catalogItem : CatalogItem,
  serial : String,
  owner : Id[Party],
  specificLocation : SpecificLocation,
  values : Seq[PropertyValue]
) extends HasId[Asset]

case class SpecificLocation(
  floor : String,
  building : String)

case class Reading(
  asset : Asset,
  time : DateTime,
  rawData : Array[Byte],
  values : Seq[PropertyValue]) 
  