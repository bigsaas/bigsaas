package org.bigsaas.core

object TenantIsolation extends Enumeration {
  val none = Value("none")
  val shared = Value("shared")
  val isolated = Value("isolated")
}

case class Tenant(name : String) extends AnyVal

case class TenantConfiguration(name : String, isolation : TenantIsolation.Value)

case class Application(name : String, version : Integer, platformVersion : (Integer, Integer), isActive : Boolean)

