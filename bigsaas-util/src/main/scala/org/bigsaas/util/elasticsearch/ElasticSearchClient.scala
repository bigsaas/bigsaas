package org.bigsaas.util.elasticsearch

import scala.concurrent.Future
import scala.concurrent.Promise

import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.Client

import spray.json.JsObject
import spray.json.JsonFormat
import spray.json.pimpAny
import spray.json.pimpString

class ElasticSearchClient(val client: Client) {

  def getById[A: JsonFormat](index: String, type_ : String, id: String): Future[A] = {
    val promise = Promise[A]()
    client.prepareGet(index, type_, id)
      .execute
      .addListener(new ActionListener[GetResponse] {
        def onResponse(response: GetResponse) {
          try response.getSourceAsString.asJson match {
            case obj: JsObject => promise.success(obj.convertTo[A])
            case x => promise.failure(new Exception("Unexpected Json document: " + x))
          }
          catch {
            case t: Throwable => promise.failure(t)
          }
        }

        def onFailure(t: Throwable) {
          promise.failure(t)
        }
      })
    promise.future
  }

  def store[A: JsonFormat](index: String, type_ : String, id: String, obj: A): Future[String] = {
    obj.toJson match {
      case obj: JsObject =>
        val promise = Promise[String]()
        client.prepareIndex(index, type_, id).
          setSource(obj.toString)
          .execute
          .addListener(new ActionListener[IndexResponse] {
            def onResponse(response: IndexResponse) {
              promise.success(response.getId())
            }

            def onFailure(e: Throwable) {
              promise.failure(e)
            }
          })
        promise.future
      case x => Future.failed(new Exception("Unexpected Json document: " + x))
    }
  }
}