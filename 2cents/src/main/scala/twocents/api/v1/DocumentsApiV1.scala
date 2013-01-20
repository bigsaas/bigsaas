package twocents.api.v1

import twocents.api.ApiDirectives
import twocents.api.ApiJsonFormats
import twocents.domain.model.Document
import twocents.logic.DocumentManager

trait DocumentsApiV1 extends ApiDirectives with ApiJsonFormats {

  val documentsApiV1 =
    path("documents") {
      authenticate { user =>
        get {
          complete {
            DocumentManager.findDocuments
          }
        }
      }
    }
}
