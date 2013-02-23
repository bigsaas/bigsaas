package org.bigsaas.core

import scala.concurrent.duration.Deadline

case class Node(name : String, active : Boolean, elasticSearch : ElasticSearchNode)

case class ElasticSearchNode(isDataNode : Boolean)


object TenantIsolation extends Enumeration {
  val none = Value("none")
  val shared = Value("shared")
  val isolated = Value("isolated")
}

case class Tenant(name : String) extends AnyVal

case class TenantConfiguration(name : String, isolation : TenantIsolation.Value)

case class Application(name : String, version : Integer, platformVersion : (Integer, Integer), isActive : Boolean)

case class RuntimeInfo(nodes : Seq[NodeInfo])

case class NodeInfo(name : String, ip : String, port : Int, applications : Seq[ApplicationNodeInfo], lastSignOfLife : Deadline)

case class ApplicationNodeInfo(name : String, version : String, clientPort : Int, lastSignOfLife : Deadline)