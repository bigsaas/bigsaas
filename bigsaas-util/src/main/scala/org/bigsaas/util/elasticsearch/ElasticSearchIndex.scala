package org.bigsaas.util.elasticsearch

case class ESIndex(index: String) extends AnyVal

sealed trait ESTypeOrAll 

case class ESType(type_ : String) extends ESTypeOrAll
case object ESTypeAll extends ESTypeOrAll

