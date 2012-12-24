package org.bigsaas.domain.assets.model

import java.util.Date

import org.bigsaas.core.model.HasId
import org.bigsaas.core.model.Id
import org.bigsaas.domain.catalog.model.CatalogItem
import org.bigsaas.domain.catalog.model.PropertyValue
import org.bigsaas.domain.party.model.Party

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
  time : Date,
  rawData : Array[Byte],
  values : Seq[PropertyValue]) 
  