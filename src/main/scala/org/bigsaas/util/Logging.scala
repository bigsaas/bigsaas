package org.bigsaas.util

import com.typesafe.config.ConfigFactory
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level
import ch.qos.logback.core.LayoutBase
import ch.qos.logback.classic.spi.ILoggingEvent
import com.typesafe.config.Config
import org.bigsaas.core.InvalidConfigrationException

trait Logging extends grizzled.slf4j.Logging {
  
  // Make logger implicit. This is useful for utility functions that take
  // a logger, like ProfilingUtils. Also strip the trailing $ from the logger
  // name.
  override implicit lazy val logger = {
    Logging.init
    grizzled.slf4j.Logger(getClass.getName.stripSuffix("$"))
  }
}

object Logging {

  val config = ConfigFactory.load
  
  lazy val init = {
    setLevel("bigsaas.loglevel", "org.bigsaas")
  }
  
  private def setLevel(key : String, logger : String) {
    LoggerFactory.getLogger(logger).asInstanceOf[Logger].setLevel {
      config.getString(key).toLowerCase match {
        case "off" => Level.OFF
        case "error" => Level.ERROR
        case "warn" => Level.WARN
        case "info" => Level.INFO
        case "debug" => Level.DEBUG
        case "trace" => Level.TRACE
        case "all" => Level.ALL
        case value => throw new InvalidConfigrationException(key, value)
      }
    }
  }
  

}

// Not being used, leave in as an example
object CustomLoggingLayout extends LayoutBase[ILoggingEvent] {

  override def doLayout(event : ILoggingEvent) = {
    s"${event.getLevel} ${event.getTimeStamp} ${event.getFormattedMessage} -- ${event.getLoggerName} [${event.getThreadName}]"
  }
}