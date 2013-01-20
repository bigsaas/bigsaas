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
import twocents.domain.model.Transaction
import twocents.domain.model.JournalEntry
import twocents.domain.model.Amount
import org.bigsaas.util.json.StringJsonFormat
import twocents.domain.model.Currency
import twocents.domain.model.DocumentKind

trait ApiJsonFormats extends DefaultJsonProtocol with CoreJsonProtocol {

  implicit object DateJsonFormat extends JsonFormat[Date] {
    def write(x: Date) = JsString(String.valueOf(x))
    def read(value: JsValue) = value match {
      case JsString(x) if x.length == 1 => new Date(0)
      case x => deserializationError("Expected Char as single-character JsString, but got " + x)
    }
  }
  
  implicit val currencyFormat = new StringJsonFormat("Currency", code => Currency(code))
  implicit val amountFormat = jsonFormat2(Amount)
  implicit val journalEntryFormat = jsonFormat5(JournalEntry)
//  implicit val accountFormat : JsonFormat[Account] = lazyFormat(jsonFormat(Account, "number", "description", "parent"))
  implicit val accountFormat2 : JsonFormat[Account] = jsonFormat4(Account)
  implicit val documentKindFormat = jsonFormat4(DocumentKind)
  implicit val transactionFormat = jsonFormat1(Transaction)
  implicit val documentFormat = jsonFormat2(Document)
  implicit val authenticationUserFormat = jsonFormat1(AuthenticationUser)
  implicit val userFormat = jsonFormat1(User)
}