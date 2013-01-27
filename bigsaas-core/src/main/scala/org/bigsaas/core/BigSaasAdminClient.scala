package org.bigsaas.core

trait BigSaasAdminClient {

  def applications : Seq[Application]
}