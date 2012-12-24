package org.bigsaas.core.model

import java.util.Locale
import java.util.UUID
import scala.collection.immutable.SortedMap
import scala.collection.immutable.TreeMap

trait HasId[A] {
  def id: Id[A]
}

object Id {
  def generate[A] = new Id[A](UUID.randomUUID.toString)
}

case class Id[A](id: String) {
  override def toString = id.toString
}

class ByLocale[T](val map : SortedMap[Locale, T]) {
  override def toString = map.toString
}

object ByLocale {
  implicit val ordering: Ordering[Locale] = Ordering.by(l => (l.getLanguage, l.getCountry, l.getVariant))
  
  def empty[T] = new ByLocale[T](SortedMap.empty)
  
  implicit def toMap[T](byLocale : ByLocale[T]) = 
    byLocale.map
    
  implicit def toByLocale[T](map : SortedMap[Locale, T]) = 
    new ByLocale(map)
    
  implicit def toByLocale[T](map : Map[Locale, T]) = 
    new ByLocale(TreeMap(map.toArray:_*))
}

