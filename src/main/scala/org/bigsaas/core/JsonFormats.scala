package org.bigsaas.core

import spray.json.DefaultJsonProtocol
import spray.json.JsonFormat
import spray.json.JsString
import spray.json.JsValue
import spray.json.deserializationError
import org.bigsaas.util.json.UtilityJsonFormats

trait JsonFormats extends DefaultJsonProtocol with UtilityJsonFormats {
  
  implicit val registeredESNodeFormat = jsonFormat1(RegisteredESNode)
  implicit val registeredNodeFormat = jsonFormat3(RegisteredNode)
  implicit val versionConstraintFormat = jsonFormat2(VersionConstraint)
  implicit val registeredApplicationFormat = jsonFormat4(RegisteredApplication)
  implicit val tenantIsolationFormat = enumerationFormat(TenantIsolation)
  implicit val registeredTenantFormat = jsonFormat2(RegisteredTenant)
  implicit val registerFormat = jsonFormat4(Register)
}