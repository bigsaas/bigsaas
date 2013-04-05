package org.bigsaas.node

import org.bigsaas.core.Register
import java.io.File
import scala.io.Source
import spray.json._
import org.bigsaas.core.JsonFormats
import java.io.FileWriter

object RegisterFile extends JsonFormats {

  val file = new File(NodeConfig.workDir, "bigsaas.conf")
  
  def read : Option[Register] = {
    if (file.exists) {
      Some(Source.fromFile(file).mkString.asJson.convertTo[Register])
    }
    else {
      None
    }
  }
  
  def write(register : Register) {
    val writer = new FileWriter(file)
    try {
      writer.write(register.toJson.prettyPrint)
    }
    finally {
      writer.close
    }
  }
}