import sbt._
import Keys._
import PlayProject._
 
object ApplicationBuild extends Build {
 
  val appName         = "bigsaas"
  val appVersion      = "1.0.1-SNAPSHOT"
 
  val appDependencies = Nil
 
  val scalaVersion = "2.9.2"
  
  val main = PlayProject(
    appName, appVersion, appDependencies, mainLang = SCALA
  ) 
 
}