import sbt._
import Keys._
import PlayProject._
 
object ApplicationBuild extends Build {
 
  val appName         = "bigsaas"
  val appVersion      = "1.0.1-SNAPSHOT"
 
  val scalaVersion = "2.9.2"
  
  val twttr = "Twitter's Repository" at "http://maven.twttr.com/"
  
  val cassie = "com.twitter" % "cassie-core" % "0.23.0"

  val appDependencies = Seq(cassie)
    
  val main = PlayProject(
    appName, appVersion, appDependencies, mainLang = SCALA
  ).settings(
      resolvers += twttr
  )
 
}