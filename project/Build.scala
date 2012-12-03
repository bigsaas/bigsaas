import sbt._
import Keys._
import PlayProject._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys

 
object BigSaasBuild extends Build {
 
  val cassie = "com.twitter" % "cassie-core" % "0.23.0" withSources()
  val sbt = "org.scala-sbt" % "sbt-api" % "0.12.0"
  val slick = "com.typesafe" % "slick_2.10.0-RC2" % "0.11.2"
  val postgresql = "postgresql" % "postgresql" % "9.1-901.jdbc4"
  val scalatest = "org.scalatest" % "scalatest_2.10.0-RC2" % "2.0.M4-B2" % "test"
  val config = "com.typesafe" % "config" % "1.0.0"
  val es = "org.elasticsearch" % "elasticsearch" % "0.20.0.RC1" withSources()
  val play = "play" %% "play" % "2.1-RC1" withSources()

  def defaultSettings =
    Project.defaultSettings ++
      Seq(
        sbtPlugin := false,
        organization := "org.bigsaas",
        version := "1.0.0-SNAPSHOT",
        scalaVersion := "2.10.0-RC3",
        publishMavenStyle := false,
        scalacOptions += "-deprecation",
        scalacOptions += "-unchecked",
        EclipseKeys.withSource := true)

  lazy val bigsaasCore = Project(id = "bigsaas-core", base = file("bigsaas-core"), settings = defaultSettings ++ Seq(
    libraryDependencies ++= Seq(es, play, scalatest, config)))

  lazy val bigsaas = Project(id = "bigsaas", base = file("."), settings = defaultSettings) aggregate(bigsaasCore)
 
}
