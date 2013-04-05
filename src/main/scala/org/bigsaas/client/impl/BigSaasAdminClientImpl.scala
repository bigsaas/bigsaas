package org.bigsaas.client.impl

import org.bigsaas.client.admin.BigSaasAdminClient
import akka.actor.ActorRef
import org.bigsaas.core.ActiveNode

class BigSaasAdminClientImpl() extends BigSaasAdminClient {
    def activeNodes : Set[ActiveNode] = Set()
}