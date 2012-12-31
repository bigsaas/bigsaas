package org.bigsaas.core

import spray.json.JsonFormat
import scala.concurrent.Future
import org.bigsaas.util.elasticsearch.ESClient
import org.bigsaas.core.model.HasId
import org.bigsaas.util.elasticsearch.ESType
import org.bigsaas.util.elasticsearch.ESIndex

trait Implicits {
		implicit def toOption[A](a : A) = Option(a)
		implicit class RichESClient(client : ESClient) {
		  def get[A: JsonFormat](index: ESIndex, type_ : ESType, hasId: HasId[A]): Future[A] = {
		    client.get[A](index, type_, hasId.id.id)
		  }
		}
}

package object implicits extends Implicits