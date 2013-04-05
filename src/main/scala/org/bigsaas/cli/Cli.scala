package org.bigsaas.cli

import org.bigsaas.client.BigSaasClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import org.bigsaas.client.impl.BigSaasClientImpl
import org.bigsaas.client.impl.BigSaasApp
import org.bigsaas.core.GetBigSaasNodeInfo
import org.bigsaas.core.ActiveNode
import org.bigsaas.util.DateTimeUtils.epoch
import org.bigsaas.util.Logging
import org.bigsaas.core.ActiveApplication
import scala.util.Success
import scala.util.Failure
import scala.util.parsing.combinator.RegexParsers

object CommandParser extends RegexParsers {
  override val skipWhitespace = false
  def COMMA   = ","
  def DQUOTE  = "\""
  def DQUOTE2 = "\"\"" ^^ { case _ => "\"" }  // combine 2 dquotes into 1
  def CRLF    = "\r\n" | "\n"
  def TXT     = "[^\",\r\n]".r
  def SPACES  = "[ \t]+".r
}

object Cli extends BigSaasApp with Logging {

  def printUsage {
    println("""
Usage: bigsaas <options> <command> <arguments>
        
Commands:
  start-node
  show-nodes
""")
  }
  
  printUsage
  
  val (options, arguments) = args.partition(_.startsWith("-"))
  arguments.toList match {
    case "start-node" :: Nil =>
      println("Starting node")
    case "show-config" :: Nil =>
    case "show-nodes" :: Nil =>
      showNodes
  }
  

  def showNodes = {
    GetBigSaasNodeInfo.ask(client.nodeActor, 5 seconds).onSuccess {
      case (nodes, apps) => 
        println("Nodes: \n  " + nodes.mkString("\n  "))
    }
  }
//  
//  var lastNodes = List[ActiveNode]()
//  var lastApplications = List[ActiveApplication]()
//  while (true) {
//    var result = GetBigSaasNodeInfo.ask(client.nodeActor, 5 seconds)
//    result.onComplete {
//      case Success((nodes, apps)) =>
//        if (lastNodes.map(_.copy(lastSignOfLife = epoch)) != nodes.map(_.copy(lastSignOfLife = epoch))) {
//          info("Nodes: \n  " + nodes.mkString("\n  "))
//        }
//        lastNodes = nodes
//        if (lastApplications.map(_.copy(lastSignOfLife = epoch)) != apps.toList.map(_.copy(lastSignOfLife = epoch))) {
//          info("Applications: \n  " + apps.mkString("\n  "))
//        }
//        lastApplications = apps
//      case Failure(t) =>
//        warn(t)
//    }
//    Thread.sleep(5000)
//  }
    
}
