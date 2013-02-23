package org.bigsaas.util

import scala.collection.JavaConversions._
import scala.math.Ordering.Implicits._

import com.typesafe.config.Config
 
object ConfigUtils {

  def mkString(config : Config, sep : String) = 
    toSeq(config).mkString(sep)
  
  def toSeq(config : Config) : Seq[String] = 
    toSeq("", config).sortWith(_ < _).map(x => x._1 + "=" + x._2)
    
  private def toSeq(prefix : String, config : Config) : Seq[(String, String)] = {
    config.entrySet.toSeq.flatMap { entry => 
      entry.getValue match {
        case config : Config => toSeq(entry.getKey + ".", config)
        case value => Seq((entry.getKey, value.render))
      }
    }
  }
}
