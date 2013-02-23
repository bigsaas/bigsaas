package org.bigsaas.util

import java.io.IOException
import java.net.Socket

object SocketUtils {
  
  def findPort(startPort : Int, count : Int) : Option[Int] = {
    val ports = startPort until startPort + count
    ports.find(portAvailable _)
  }
  
  /**
   * Checks to see if a specific port is available.
   *
   * @param port the port to check for availability
   */
  def portAvailable(port : Int) : Boolean = {
    try {
      val socket = new Socket("localhost", port)
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