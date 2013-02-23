package org.bigsaas.node

import akka.actor.Actor
import akka.actor.ActorRef
import org.bigsaas.core.Application

/**
 * Each application has an representative actor on each node.
 */
class BigSaasNodeApplicationActor(application : Application, nodeActor : ActorRef) extends Actor {

  def receive = {
    case "" =>
  }
}