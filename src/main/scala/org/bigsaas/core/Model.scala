package org.bigsaas.core

import java.util.UUID
import org.bigsaas.util.model.Id
import akka.actor.ActorRef
import org.joda.time.DateTime

case class Register(nodes : List[RegisteredNode], applications : List[RegisteredApplication], tenants : List[RegisteredTenant], lastUpdated : DateTime)

case class RegisteredNode(name : String, active : Boolean, elasticSearch : RegisteredESNode)

case class RegisteredESNode(isDataNode : Boolean)


object TenantIsolation extends Enumeration {
  val none = Value("none")
  val shared = Value("shared")
  val isolated = Value("isolated")
}

case class Tenant(id : Id[Tenant], name : String)

case class RegisteredTenant(name : String, isolation : TenantIsolation.Value)

case class UrlDomain(domain : String, pathPrefix : Option[String])

case class RegisteredApplication(name : String, version : Int, platformVersion : VersionConstraint, isActive : Boolean)

case class VersionConstraint(from : Int, to : Int)

case class ActiveNode(id : Id[ActiveNode], name : String, actorRef : ActorRef, startedApplications : Map[String, Id[ActiveApplication]], lastSignOfLife : DateTime)

object ActiveApplicationStatus extends Enumeration {
  val GREEN = Value("green")
  val YELLOW = Value("yellow")
  val RED = Value("red")
} 

case class ActiveApplication(id : Id[ActiveApplication], name : String, version : String, actorRef : ActorRef, lastSignOfLife : DateTime, status : ActiveApplicationStatus.Value, message : Option[String] = None)