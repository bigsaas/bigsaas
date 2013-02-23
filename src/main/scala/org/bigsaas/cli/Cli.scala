package org.bigsaas.cli

import org.bigsaas.client.BigSaasClient
import scala.concurrent.ExecutionContext.Implicits.global


object Cli extends App {

  while (true) {
    BigSaasClient.admin.runtimeInfo.map(info => println(info))
    Thread.sleep(1000)
  }
}