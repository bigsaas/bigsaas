package org.bigsaas.util.elasticsearch

import scala.concurrent.Future
import org.elasticsearch.action.Action
import org.elasticsearch.action.ActionRequest
import org.elasticsearch.action.ActionRequestBuilder
import org.elasticsearch.action.ActionResponse
import org.elasticsearch.action.admin.cluster.ClusterAction
import org.elasticsearch.action.admin.cluster.health.ClusterHealthAction
import org.elasticsearch.action.count.CountAction
import org.elasticsearch.action.get.GetAction
import org.elasticsearch.action.get.MultiGetAction
import org.elasticsearch.action.index.IndexAction
import org.elasticsearch.action.search.MultiSearchAction
import org.elasticsearch.action.search.SearchAction
import org.elasticsearch.action.search.SearchScrollAction
import org.elasticsearch.client.Client
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoAction
import org.elasticsearch.action.ActionListener
import scala.concurrent.Promise

trait ActionMagnet[Request, Response] {
  def execute(javaClient: Client, request: Request): Future[Response]
}

object ActionMagnet {

  implicit val getAction = magnet(GetAction.INSTANCE)
  implicit val multiGetAction = magnet(MultiGetAction.INSTANCE)
  implicit val countAction = magnet(CountAction.INSTANCE)
  implicit val searchAction = magnet(SearchAction.INSTANCE)
  implicit val searchScrollAction = magnet(SearchScrollAction.INSTANCE)
  implicit val multiSearchAction = magnet(MultiSearchAction.INSTANCE)
  implicit val indexAction = magnet(IndexAction.INSTANCE)
  implicit val clusterHealthAction = magnet(ClusterHealthAction.INSTANCE)
  implicit val nodesInfoAction = magnet(NodesInfoAction.INSTANCE)

  private def magnet[Request <: ActionRequest[Request], Response <: ActionResponse, RequestBuilder <: ActionRequestBuilder[Request, Response, RequestBuilder]](action: Action[Request, Response, RequestBuilder]) =
    new ActionMagnet[Request, Response] {
      def execute(javaClient: Client, request: Request) = {
        val promise = Promise[Response]()
        javaClient.execute(action, request, actionListener(promise))
        promise.future
      }
    }
  
  private def magnet[Request <: ActionRequest[Request], Response <: ActionResponse, RequestBuilder <: ActionRequestBuilder[Request, Response, RequestBuilder]](action: ClusterAction[Request, Response, RequestBuilder]) =
    new ActionMagnet[Request, Response] {
      def execute(javaClient: Client, request: Request) = {
        val promise = Promise[Response]()
        javaClient.admin.cluster.execute(action, request, actionListener(promise))
        promise.future
      }
    }

  private def actionListener[A](promise: Promise[A]) = new ActionListener[A] {
    def onResponse(response: A) {
      promise.success(response)
    }
    def onFailure(e: Throwable) {
      promise.failure(e)
    }
  }
}
