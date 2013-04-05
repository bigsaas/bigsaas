package org.bigsaas.node

import akka.actor.Actor
import akka.actor.ActorRef
import org.bigsaas.core.RegisteredApplication

/**
 * Each application has an representative actor on each node.
 */
class BigSaasNodeApplicationActor(application : RegisteredApplication, nodeActor : ActorRef) extends Actor {

  def receive = {
    case "" =>
  }
}