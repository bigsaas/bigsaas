package org.bigsaas.client.impl

case class ApplicationStartupParameters(
    platformVersion : Int, 
    httpPort : Int, 
    httpsPort : Int, 
    actorPort : Int, 
    applicationActorPath : String)