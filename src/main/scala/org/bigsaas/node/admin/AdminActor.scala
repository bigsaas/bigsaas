package org.bigsaas.node.admin

import spray.can.server.SprayCanHttpServerApp
import akka.actor.actorRef2Scala
import akka.actor.Actor
import org.jboss.netty.channel.group.DefaultChannelGroup

class AdminActor extends Actor {

  private val socketConnections = new DefaultChannelGroup()

  def receive = {
    case s => println(s) 
  } 
}
