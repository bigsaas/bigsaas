package org.bigsaas.client

import org.bigsaas.core.ActiveApplication
import org.bigsaas.util.Config
import org.bigsaas.util.model.Id

object ApplicationConfig extends Config("bigsaas.application") {
  val id = stringOption("id").map(Id[ActiveApplication](_))
  val name = string("name")
  val version = string("version")
  val lastSignOfLifeSec = int("last-sign-of-life-sec")
}