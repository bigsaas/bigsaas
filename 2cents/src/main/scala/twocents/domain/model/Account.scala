package twocents.domain.model

import org.bigsaas.core.model.Id

case class Administration(ledger : Ledger, files : Seq[DocumentFile], kinds : Seq[DocumentKind])

/**
 * Grootboek.
 */
case class Ledger(accounts: Seq[Account])

/**
 * Grootboekrekening.
 */
case class Account(id : Id[Account] = Id.generate, number : String, description : String, parent : Option[Account])

/**
 * A chart of accounts (COA) is a created list of the accounts used by a business entity to define each class of items for which money or the equivalent is spent or received. It is used to organize the finances of the entity and to segregate expenditures, revenue, assets and liabilities in order to give interested parties a better understanding of the financial health of the entity.
 */
case class ChartOfAccounts()

case class DocumentFile(id : Id[DocumentFile])

/**
 * 'Bon', like invoice.
 */
case class Document(documentKind : Id[DocumentKind], transactions : Seq[Transaction])

case class DocumentKind(id : Id[DocumentKind], name : String, files : Seq[Id[DocumentFile]], defaultVat : Id[Vat])

case class Transaction(entries : Seq[JournalEntry])

case class JournalEntry(id : Id[Document], debitAccount : Id[Account], debit : Amount, creditAccount : Id[Account], credit : Amount)

case class Vat(id : Id[Vat], name : String, perc: Double)

case class Amount(value : BigDecimal, currency: Currency)

/**
 * ISO 4217 
 */
case class Currency(code : String) extends AnyVal {
  def symbol : String = java.util.Currency.getInstance(code).getSymbol()
}

