package org.bigsaas.node

import java.net.InetAddress
import org.bigsaas.client.impl.BigSaasClientImpl
import org.bigsaas.core.BigSaasConfig
import org.bigsaas.core.BigSaasConfig.toConfig
import org.bigsaas.util.ConfigUtils
import org.bigsaas.util.Logging
import com.typesafe.config.ConfigFactory
import org.bigsaas.core.NodeAlreadyRunningException

object Main extends App with Logging {

  info("Configuration: \n  " + ConfigUtils.toSeq(BigSaasConfig).filter(_.startsWith("bigsaas")).mkString("\n  ") + "\n")
  debug("Full Configuration: \n  " + ConfigUtils.mkString(BigSaasConfig, "\n  ") + "\n")

  try {
    BigSaasNode.start
  }
  catch {
    case e : NodeAlreadyRunningException => error(e.getMessage)
  }
  
}