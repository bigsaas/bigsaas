package twocents.api

import java.sql.Date
import spray.json.DefaultJsonProtocol
import spray.json.JsString
import spray.json.JsValue
import spray.json.JsonFormat
import spray.json.deserializationError
import twocents.domain.model.Account
import twocents.domain.model.Document
import org.bigsaas.core.json.CoreJsonProtocol
import twocents.api.v1.AuthenticationUser
import twocents.domain.model.User

trait ApiJsonFormats extends DefaultJsonProtocol with CoreJsonProtocol {

  implicit object DateJsonFormat extends JsonFormat[Date] {
    def write(x: Date) = JsString(String.valueOf(x))
    def read(value: JsValue) = value match {
      case JsString(x) if x.length == 1 => new Date(0)
      case x => deserializationError("Expected Char as single-character JsString, but got " + x)
    }
  }
  
  implicit val accountFormat = jsonFormat1(Account)
  implicit val documentFormat = jsonFormat1(Document)
  implicit val authenticationUserFormat = jsonFormat1(AuthenticationUser)
  implicit val userFormat = jsonFormat1(User)
}