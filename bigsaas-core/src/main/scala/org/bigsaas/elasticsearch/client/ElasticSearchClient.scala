package org.bigsaas.elasticsearch.client

import scala.annotation.implicitNotFound
import scala.concurrent.Future
import scala.concurrent.Promise
import org.bigsaas.core.BigSaasConfig
import org.bigsaas.core.BigSaasConfig.toConfig
import org.bigsaas.core.Datastore
import org.bigsaas.core.model.Id
import org.elasticsearch.action.ActionListener
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.Client
import play.api.libs.json.JsResultException
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import org.bigsaas.core.Tenant

object ElasticSearchClient {

  val config = BigSaasConfig.getConfig("elasticsearch")
}

class ElasticSearchClient(client: Client) {
  
  def datastore(index : String, typ : String)(implicit tenant : Tenant) = 
    new ElasticSearchDatastore(client, index, typ)
  
}

class ElasticSearchDatastore(client: Client, index : String, typ : String) extends Datastore {

  def get[A](id: Id[A])(implicit reads: Reads[A]): Future[A] = {
    val promise = Promise[A]()
    client.prepareGet(index, typ, id.id)
      .execute
      .addListener(new ActionListener[GetResponse] {
        def onResponse(response: GetResponse) {
          val fromJsonResult = Json.fromJson(Json.parse(response.getSourceAsString))
          fromJsonResult.fold(
            valid = res =>
              promise.success(res),
            invalid = errors =>
              promise.failure(JsResultException(errors)));
        }

        def onFailure(e: Throwable) {
          promise.failure(e)
        }
      })
    promise.future
  }

  def store[A](id: Id[A], obj: A)(implicit writes: Writes[A]): Future[Id[A]] = {
    val promise = Promise[Id[A]]()
    client.prepareIndex(index, typ, id.id).
      setSource(Json.toJson(obj).toString)
      .execute
      .addListener(new ActionListener[IndexResponse] {
        def onResponse(response: IndexResponse) {
          promise.success(Id[A](response.getId()))
        }

        def onFailure(e: Throwable) {
          promise.failure(e)
        }
      })
    promise.future
  }
}