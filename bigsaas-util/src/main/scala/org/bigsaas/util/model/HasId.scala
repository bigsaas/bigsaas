package org.bigsaas.util.model

import java.util.UUID

trait HasId[A] {
  def id: Id[A]
}

object Id {
  def generate[A] = new Id[A](UUID.randomUUID.toString)
}

case class Id[A](id: String) {
  override def toString = id.toString
}