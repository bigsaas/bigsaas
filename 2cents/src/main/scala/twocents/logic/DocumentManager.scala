package twocents.logic

import twocents.domain.model.Document
import org.bigsaas.core.model.Id

object DocumentManager {

  def findDocuments : Seq[Document] = {
    Seq(Document(Id.generate, Seq()))
  }
}