package org.bigsaas.util.elasticsearch

import scala.concurrent.Future

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse
import org.elasticsearch.action.count.CountRequest
import org.elasticsearch.action.count.CountResponse
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.get.MultiGetRequest
import org.elasticsearch.action.get.MultiGetResponse
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.MultiSearchRequest
import org.elasticsearch.action.search.MultiSearchResponse
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.search.SearchScrollRequest
import org.elasticsearch.client.Client

class ESClient(val javaClient: Client) {

  def apply[Request, Response](request: Request)(implicit action: ActionMagnet[Request, Response]): Future[Response] =
    action.execute(javaClient, request)

  def get(index: String, id: String): Future[GetResponse] =
    this(new GetRequest(index, null, id))

  def get(index: String, type_ : String, id: String): Future[GetResponse] =
    this(new GetRequest(index, type_, id))

  def get(request: GetRequest): Future[GetResponse] =
    this(request)

  def multiGet(request: MultiGetRequest): Future[MultiGetResponse] =
    this(request)

  def count(indices: String*): Future[CountResponse] =
    this(new CountRequest(indices: _*))

  def count(request: CountRequest): Future[CountResponse] =
    this(request)

  def search(indices: String*): Future[SearchResponse] =
    this(new SearchRequest(indices: _*))

  def search(request: SearchRequest): Future[SearchResponse] =
    this(request)

  def searchScroll(scrollId: String): Future[SearchResponse] =
    this(new SearchScrollRequest(scrollId))

  def searchScroll(request: SearchScrollRequest): Future[SearchResponse] =
    this(request)

  def multiSearch(request: MultiSearchRequest): Future[MultiSearchResponse] =
    this(request)

  def index(index: String): Future[IndexResponse] =
    this(new IndexRequest(index))

  def index(index: String, type_ : String): Future[IndexResponse] =
    this(new IndexRequest(index, type_, null))

  def index(index: String, type_ : String, id: String): Future[IndexResponse] =
    this(new IndexRequest(index, type_, id))

  def index(request: IndexRequest): Future[IndexResponse] =
    this(request)

  def clusterHealth(indices: String*): Future[ClusterHealthResponse] =
    this(new ClusterHealthRequest(indices: _*))

  def clusterHealth(request: ClusterHealthRequest) =
    this(request)

  def clusterNodesInfo(indices: String*): Future[NodesInfoResponse] =
    this(new NodesInfoRequest(indices: _*))

  def clusterNodesInfo(request: NodesInfoRequest) =
    this(request)
}