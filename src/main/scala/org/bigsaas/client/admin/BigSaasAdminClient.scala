package org.bigsaas.client.admin
import org.bigsaas.core.ActiveNode

trait BigSaasAdminClient {

  def activeNodes : Set[ActiveNode]
  
}
