package org.bigsaas.util

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet
import scala.math.Ordering.Implicits._

import com.typesafe.config.{Config => C}
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigValue
import com.typesafe.config.ConfigValueType
 
object Config extends Config(ConfigFactory.load, Nil) {
  def merge(configString : String) = ConfigFactory.parseString(configString).withFallback(config)
}

class Config(val config : C, val reverseName : List[String]) {
  def this(name : String) = this(Config(name).config, name :: Nil)
  def apply(name : String) = new Config(config.getConfig(name), name :: reverseName)
  
  lazy val fullName = reverseName.reverse.mkString(".")
  
  def int(key : String) = 
    value(key, "Should be a valid integer.")(_ => config.getInt(key))
    
  def intOption(key : String) = 
    value(key, "Should be a valid integer.")(_ => stringOption(key).map(_.toInt))
    
  def string(key : String) = 
    config.getString(key)

  def stringOption(key : String) = 
    Option(config.getString(key)).map(_.trim).filter(_.nonEmpty)

  def stringList(key: String) : List[String] = 
    value(key, "Should be a comma-separated list of strings.")(stringList(key, _))
  
  def intList(key: String) : List[Int] = 
    value(key, "Should be a comma-separated list of integers.")(stringList(key, _).map(_.toInt))
      
  def intRange(key : String) = 
    value(key, "Range should consist of two integers.")(stringList(key, _).map(_.toInt) match {
      case i1 :: i2 :: Nil => (i1, i2)
      case _ => throw new Exception
    })

  def anyOf(key : String, possibleValues : List[String]) = 
    value(key, s"Value should be any of: ${possibleValues.mkString(", ")}") { _ => config.getString(key) match {
      case s if possibleValues.contains(s) => s
      case _ => throw new Exception
    }}
  
  def mkString(sep : String) = 
    toSeq.mkString(sep)
  
  def toSeq : Seq[String] = 
    toSeq("").sortWith(_ < _).map(x => x._1 + "=" + x._2)
    
  private def toSeq(prefix : String) : Seq[(String, String)] = {
    config.entrySet.toSeq.flatMap { entry => 
      entry.getValue match {
        case config : Config => toSeq(entry.getKey + ".")
        case value => Seq((entry.getKey, value.render(ConfigRenderOptions.concise)))
      }
    }
  }
    
  private def stringList(key : String, value : ConfigValue) = value match {
    case s if s.valueType == ConfigValueType.STRING => config.getString(key).replaceAll("\\\"|\\[|\\]", "").split(",").toList
    case s if s.valueType == ConfigValueType.LIST => config.getStringList(key).map(_.toString).toList
  }
  
  private def value[A](key : String, explain : String)(f : ConfigValue => A) = {
    val value = try {
      config.getValue(key)
    }  catch {
      case t : Throwable => 
        throw new MissingConfigrationValueException(fullName + "." + key)
    }
    try {
      f(value)
    }  catch {
      case t : Throwable => 
        throw new InvalidConfigrationValueException(fullName + "." + key, value, explain)
    }
  }
}

class InvalidConfigrationException(val key : String, val message : String) extends Exception(message)
class InvalidConfigrationValueException(key : String, val value : ConfigValue, val explanation : String) extends InvalidConfigrationException(key, s"Invalid configuration $key=${value.render(ConfigRenderOptions.concise)}: $explanation")
class MissingConfigrationValueException(key : String) extends InvalidConfigrationException(key, s"Missing configuration value: $key")
