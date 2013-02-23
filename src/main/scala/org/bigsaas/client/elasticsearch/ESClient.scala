package org.bigsaas.client.elasticsearch

//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.bigsaas.util.model.Id
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import spray.json.JsObject
import spray.json.JsonFormat
import spray.json.pimpAny
import spray.json.pimpString
import collection.JavaConversions._
import scala.concurrent.ExecutionContext
import org.scalastuff.esclient.{ESClient => ESClient2}
import org.elasticsearch.client.Client

class ESClient(val client: Client) {

  def javaClient = client.javaClient

  def get[A: JsonFormat](index: ESIndex, type_ : ESTypeOrAll, id: Id[A])(implicit ec : ExecutionContext): Future[A] = {
    val t = type_ match {
      case ESTypeAll => "_all"
      case ESType(t) => t
    }
    client.execute(new GetRequest(index.index, t, id.toString)).map(_.getSourceAsString.asJson match {
      case obj: JsObject => obj.convertTo[A]
      case x => throw new Exception("Unexpected Json document: " + x)
    })
  }

  def index[A: JsonFormat](index: ESIndex, typ: ESType, id: Id[A], obj: A)(implicit ec : ExecutionContext) : Future[IndexResponse] = {
    obj.toJson match {
      case obj: JsObject =>
        client.execute(new IndexRequest(index.index, typ.type_, id.toString).source(obj.toString))
      case x => Future.failed(new Exception("Unexpected Json document: " + x))
    }
  }
}