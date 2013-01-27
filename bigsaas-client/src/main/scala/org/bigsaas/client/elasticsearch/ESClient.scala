package org.bigsaas.client.elasticsearch

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.bigsaas.core.model.Id
import org.bigsaas.util.elasticsearch.{ESClient => ScalaClient}
import org.bigsaas.util.elasticsearch.ESFuture
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import spray.json.JsObject
import spray.json.JsonFormat
import spray.json.pimpAny
import spray.json.pimpString
import collection.JavaConversions._

case class ESClient(val scalaClient: ScalaClient) {

  def javaClient = scalaClient.javaClient

  def get[A: JsonFormat](index: ESIndex, type_ : ESTypeOrAll, id: Id[A]): Future[A] = {
    val t = type_ match {
      case ESTypeAll => "_all"
      case ESType(t) => t
    }
    scalaClient.get(new GetRequest(index.index, t, id.toString)).map(_.getSourceAsString.asJson match {
      case obj: JsObject => obj.convertTo[A]
      case x => throw new Exception("Unexpected Json document: " + x)
    })
  }

  def index[A: JsonFormat](index: ESIndex, typ: ESType, id: Id[A], obj: A) = ESFuture[IndexResponse] {
    obj.toJson match {
      case obj: JsObject =>
        javaClient.index(new IndexRequest(index.index, typ.type_, id.toString).source(obj.toString), _)
      case x => throw new Exception("Unexpected Json document: " + x)
    }
  }
}