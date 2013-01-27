
package org.bigsaas.core

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

class BigSaasConfig {
  val config = ConfigFactory.load.getConfig("bigsaas")
}

object BigSaasConfig extends BigSaasConfig {
  implicit def toConfig(config : BigSaasConfig) : Config = config.config 
}