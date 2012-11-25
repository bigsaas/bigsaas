package org.bigsaas.core.model

import java.util.Locale
import java.util.UUID
import org.bigsaas.domain.assetmanagement.Asset

trait HasId[A] {
  def id: Id[A]
}

object Id {
  def generate[A] = new Id[A](UUID.randomUUID.toString)
}

case class Id[A](id: String) 


