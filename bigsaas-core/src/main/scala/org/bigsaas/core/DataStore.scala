package org.bigsaas.core

import play.api.libs.json.Reads
import scala.concurrent.Future
import play.api.libs.json.Writes
import org.bigsaas.core.model.HasId
import org.bigsaas.core.model.Id

trait Datastore {

  def get[A](id: Id[A])(implicit reads: Reads[A]): Future[A]
  
  def store[A](id: Id[A], obj : A)(implicit writes: Writes[A]): Future[Id[A]]
  
  def store[A <: HasId[A]](obj : A)(implicit writes: Writes[A]): Future[Id[A]] = 
  	store(obj.id, obj)
}