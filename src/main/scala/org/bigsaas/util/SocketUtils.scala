package org.bigsaas.util

import java.io.IOException
import java.net.Socket
import java.net.InetAddress

object SocketUtils {
  
  def ip = InetAddress.getLocalHost.getHostAddress
  
  def findPort(portRange : (Int, Int)) : Option[Int] = {
    val ports = portRange._1 until portRange._2
    ports.find(portAvailable _)
  }

  /**
   * Checks to see if a specific port is available.
   *
   * @param port the port to check for availability
   */
  def portAvailable(port : Int) : Boolean = {
    try {
      val socket = new Socket(InetAddress.getLocalHost.getHostAddress, port)
      try {
        socket.close
      } catch {
        case _ : IOException => 
      }
      false
    } catch {
      case _ : IOException => true
    }
  }
}