package org.bigsaas.client.impl

import akka.actor.ActorRef
import akka.actor.Actor

class BigSaasApplicationActor(nodeActor : ActorRef) extends Actor {

  def receive = {
    case "" =>
  }
}