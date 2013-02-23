package org.bigsaas.util.model

import java.util.Locale

import scala.collection.immutable.SortedMap
import scala.collection.immutable.TreeMap

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

