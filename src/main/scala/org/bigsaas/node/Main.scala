package org.bigsaas.node

import org.bigsaas.core.NodeAlreadyRunningException
import org.bigsaas.util.Config
import org.bigsaas.util.Logging

import com.typesafe.config.ConfigException

object Main extends App with Logging {

  try {
    info("Configuration: \n  " + Config.toSeq.filter(_.startsWith("bigsaas")).mkString("\n  ") + "\n")
    debug("Full Configuration: \n  " + Config.mkString("\n  ") + "\n")

    BigSaasNode.start
  }
  catch {
    case e : NodeAlreadyRunningException => error(e.getMessage)
    case e : ConfigException => error(e.getMessage)
  }
  
}