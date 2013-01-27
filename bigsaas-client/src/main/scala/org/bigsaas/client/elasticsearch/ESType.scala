package org.bigsaas.client.elasticsearch

sealed trait ESTypeOrAll 

case class ESType(type_ : String) extends ESTypeOrAll
case object ESTypeAll extends ESTypeOrAll

