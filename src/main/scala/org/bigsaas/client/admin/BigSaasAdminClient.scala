package org.bigsaas.client.admin

import scala.concurrent.Future
import org.bigsaas.core.RuntimeInfo

trait BigSaasAdminClient {

  def runtimeInfo : Future[RuntimeInfo]
  
}