package org.bigsaas.core.node

import org.bigsaas.core.BigSaasAdminClient
import org.bigsaas.core.Application

class BigSaasAdminClientImpl(client : BigSaasNodeClient) extends BigSaasAdminClient {

    def applications : Seq[Application] = Seq.empty

}