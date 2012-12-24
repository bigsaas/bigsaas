package org.bigsaas.util.elasticsearch

import org.elasticsearch.client.Client

trait Implicits {
  
  implicit def richElasticSearchClient(client : Client) =
    new ElasticSearchClient(client)
}

package object implicits extends Implicits