package org.bigsaas.core

import akka.pattern.Patterns
import akka.actor.ActorRef
import scala.concurrent.Future
import akka.util.Timeout

trait AkkaRequest[Response] extends Serializable {
  def ask(actor : ActorRef, timeout : Timeout) : Future[Response] = 
    Patterns.ask(actor, this, timeout).asInstanceOf[Future[Response]]
}

object GetBigSaasNodeInfo extends AkkaRequest[(List[ActiveNode], List[ActiveApplication])]

case class NodeSignOfLife(name : String, ip : String, port : Int, applications : List[ActiveApplication])
case class ApplicationSignOfLife(applicationName : String, applicationVersion : String, clientPort : Int, message : Option[String])

