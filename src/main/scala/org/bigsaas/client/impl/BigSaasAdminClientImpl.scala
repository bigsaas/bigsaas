package org.bigsaas.client.impl

import akka.actor.ActorRef
import org.bigsaas.client.admin.BigSaasAdminClient
import akka.actor.ActorSystem
import scala.concurrent.Future
import org.bigsaas.core.RuntimeInfo
import akka.pattern.Patterns
import org.bigsaas.core.RuntimeInfoRequest
import scala.concurrent.duration._

class BigSaasAdminClientImpl(system : ActorSystem, nodeActor : ActorRef) extends BigSaasAdminClient {

  def runtimeInfo : Future[RuntimeInfo] = {
    RuntimeInfoRequest.ask(nodeActor, 5 seconds)
  }
}