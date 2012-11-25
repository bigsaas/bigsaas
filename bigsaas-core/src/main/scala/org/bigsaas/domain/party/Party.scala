package org.bigsaas.domain.party

import org.bigsaas.core.model.Id
import org.bigsaas.domain.catalog.CatalogItem
import org.bigsaas.util.Implicits._
import scala.util.Random

case class Party (
  id : Id[Party] = Id.generate,
  owner : Option[Id[Party]] = None, 
  
  /**
   * name of the party
   */
  name : String,
  
  addresses : Seq[Address] = Seq.empty,
  phoneNumbers : Seq[PhoneNumber] = Seq.empty,
  emailAddresses : Seq[Email] = Seq.empty,

  /**
   * Party is allowed to create assets and child-models of these models.
   * Party can see all child models and their properties.
   * Party can see all properties of parent models.
   * Party can only change models it owns (AssetModel.owner)
   */
  visibleAssetModels : Set[Id[CatalogItem]] = Set.empty,
  
  relations : Set[Party] = Set.empty,
  
  /**
   * Users of the party
   */
  users : Map[Id[User], User] = Map.empty,
  
  roles : Set[PartyRole] = Set.empty
)

trait PartyRole

case class OwnedParty (
  party : Id[Party]
)

case class Relation (
  relatedParty : Id[Party],
  relatedPartyAlias : String,
  relationType : String, 
  allowedAssetModels : Seq[Id[CatalogItem]]
)

case class UserRole (
  name : String,
  permissions : Seq[String]
)

case class User (
  id : Id[User] = Id.generate,
  logins : Set[Id[Login]] = Set.empty,
  firstName : String = "",
  middleName : String = "",
  lastName : String = "",
  addresses : Seq[Address] = Seq(),
  phoneNumbers : Seq[PhoneNumber] = Seq.empty,
  emailAddresses : Seq[Email] = Seq.empty,
  roles : Set[String] = Set.empty
)
  
case class Address (
  kind : Option[AddressKind.Value] = None,
  street: Option[String] = None, 
  street2: Option[String] = None, 
  postalCode: Option[String] = None, 
  city: Option[String] = None, 
  country : Option[String] = None)

object AddressKind extends Enumeration {
  val Home = Value("home")
  val Work = Value("work")
  val Invoice = Value("invoice")
  val Delivery = Value("delivery")
  val Other = Value("other")
}

case class PhoneNumber (
  kind : Option[PhoneNumberKind.Value] = None,
  phoneNumber : String)

object PhoneNumberKind extends Enumeration {
  val Home = Value("home")
  val Work = Value("work")
  val Other = Value("other")
  def other(tag : String) = Value(tag)
}

case class Email (
  kind : Option[EmailKind.Value] = None,
  email : String
)

object EmailKind extends Enumeration {
  val Home = Value("home")
  val Work = Value("work")
  val Other = Value("other")
}

object Login {
  def password(salt : String, password : String) = (salt + password).sha256Hash
}

case class Login (
  id : Id[Login] = Id.generate,
  email : String, 
  salt : String = Random.nextString(8),
  password : Array[Byte] = Array.empty,
  lastChosenParty : Option[Id[Party]] = None)

