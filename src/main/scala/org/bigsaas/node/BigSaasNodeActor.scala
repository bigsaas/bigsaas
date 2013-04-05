package org.bigsaas.node

import scala.collection.mutable

import org.bigsaas.core.ActiveApplication
import org.bigsaas.core.ActiveNode
import org.bigsaas.core.GetBigSaasNodeInfo
import org.bigsaas.core.Register
import org.bigsaas.util.Logging
import org.bigsaas.util.model.Id
import org.bigsaas.util.DateTimeUtils.epoch
import org.joda.time.DateTime
import scala.concurrent.duration._
import akka.actor.Actor


/**
 * A node in the BigSaas cluster.
 */
class BigSaasNodeActor extends Actor with Logging {

  import context.dispatcher 
  
  info("BigSaas Node started")
  
  val nodeId = Id.generate[ActiveNode]

  var currentRegister : Register = Register(Nil, Nil, Nil, new DateTime(0))
  val activeNodes = mutable.Map[Id[ActiveNode], ActiveNode]()
  val activeApplications = mutable.Map[Id[ActiveApplication], ActiveApplication]()
  
  def activeNode = ActiveNode(nodeId, NodeConfig.name, self, Map(), DateTime.now)

  override def preStart {
    // Read register from file
    RegisterFile.read.foreach(self ! _)

    // Periodically send a sign of life to the other nodes.
    context.system.scheduler.schedule(0 seconds, NodeConfig.lastSignOfLifeSec seconds) {
      self ! activeNode
    }
  }
  
  def receive = {
    case register : Register =>
      // do we need to update the register?
      val registerChanged = 
        if (register.lastUpdated.isAfter(currentRegister.lastUpdated)) true
        else if (register.lastUpdated.isEqual(currentRegister.lastUpdated) && register != currentRegister) true
        else false
      if (registerChanged) {
        info("Register changed: " + register)
        currentRegister = register
        RegisterFile.write(register)
        activeNodes.values.foreach { node =>
          node.actorRef ! register
        }
      }
    case node : ActiveNode =>
      val oldNode = activeNodes.put(node.id, node)
      if (oldNode != Some(node)) {
        if (oldNode.map(_.copy(lastSignOfLife=epoch)) != Some(node.copy(lastSignOfLife=epoch)))
          info("Active node changed: " + node)
        activeNodes.values.foreach { node =>
          node.actorRef ! node
          activeApplications.values.foreach { app =>
            node.actorRef ! app}
        }
      }
    case app : ActiveApplication =>
      val oldApp = activeApplications.put(app.id, app) 
      if (oldApp != Some(app)) {
        if (oldApp.map(_.copy(lastSignOfLife=epoch)) != Some(app.copy(lastSignOfLife=epoch)))
          info("Active application changed: " + app)
        activeNodes.values.foreach { node =>
          node.actorRef ! app
        }
      }
    case GetBigSaasNodeInfo => 
      sender ! (activeNodes.values.toList, activeApplications.values.toList)
      
      
//    case RuntimeInfoRequest => sender ! runtimeInfo
//    case NodeSignOfLife(name, ip, port, applications) =>
//      nodeInfo += (name, ip, port) -> ActiveNode(NodeId(name, port), ip, applications, Deadline.now)
//    case ApplicationSignOfLife(name, version, clientPort, message) =>
//      applicationNodeInfo += (name, version, clientPort) -> ActiveApplication(name, version, clientPort, NodeId(name, port), Deadline.now)
    case s => info("Received Other command" + s)
  }
}