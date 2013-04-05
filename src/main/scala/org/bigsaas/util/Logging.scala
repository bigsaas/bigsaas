package org.bigsaas.util

import org.slf4j.LoggerFactory

import com.typesafe.config.ConfigFactory

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.LayoutBase

trait Logging extends grizzled.slf4j.Logging {
  
  // Make logger implicit. This is useful for utility functions that take
  // a logger, like ProfilingUtils. Also strip the trailing $ from the logger
  // name.
  override implicit lazy val logger = {
    grizzled.slf4j.Logger(getClass.getName.stripSuffix("$"))
  }
}

object Logging {
  def setLevel(key : String, logger : String) {
    LoggerFactory.getLogger(logger).asInstanceOf[Logger].setLevel {
      Config.anyOf(key, "off" :: "error" :: "warn" :: "info" :: "debug" :: "trace" :: "all" :: Nil) match {
        case "off" => Level.OFF
        case "error" => Level.ERROR
        case "warn" => Level.WARN
        case "info" => Level.INFO
        case "debug" => Level.DEBUG
        case "trace" => Level.TRACE
        case "all" => Level.ALL
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