import sbt._
import Keys._
import PlayProject._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseCreateSrc
 
object BigSaasBuild extends Build {
 
  val cassie = "com.twitter" % "cassie-core" % "0.23.0" withSources()
  val sbt = "org.scala-sbt" % "sbt-api" % "0.12.0"
  val slick = "com.typesafe" % "slick_2.10.0-RC2" % "0.11.2" withSources
  val postgresql = "postgresql" % "postgresql" % "9.1-901.jdbc4"
  val scalatest = "org.scalatest" % "scalatest_2.10.0-RC2" % "2.0.M4-B2" % "test"
  val guava = "com.google.guava" % "guava" % "12.0.1" withSources()
  val config = "com.typesafe" % "config" % "1.0.0"
  val es = "org.elasticsearch" % "elasticsearch" % "0.20.4" withSources()
//  val play = "play" %% "play" % "2.1-RC1" withSources()
  val grizzled = "org.clapper" %% "grizzled-slf4j" % "1.0.1"
  val logback = "ch.qos.logback" % "logback-classic" % "1.0.9"
  val sprayCan = "io.spray" % "spray-can" % "1.1-M7"
  val sprayRouting = "io.spray" % "spray-routing" % "1.1-M7"
  val sprayJson = "io.spray" %% "spray-json" % "1.2.3"
  val sprayTest = "io.spray" % "spray-testkit" % "1.1-M7" % "test"
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.1.0" 
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" % "2.1.0" 
  val curator = "com.netflix.curator" % "curator-framework" % "1.2.6" exclude("org.slf4j","slf4j-log4j12")
  val esclient = "org.scalastuff" %% "esclient" % "0.20.3"

  def defaultSettings =
    Project.defaultSettings ++
      Seq(
        sbtPlugin := false,
        organization := "org.bigsaas",
        version := "1.0.0-SNAPSHOT",
        scalaVersion := "2.10.0",
        publishMavenStyle := false,
        scalacOptions += "-deprecation",
        scalacOptions += "-unchecked",
        libraryDependencies += scalatest,
        resolvers += "spray repo" at "http://repo.spray.io",
	resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
        EclipseKeys.skipParents in ThisBuild := true,
        EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
        EclipseKeys.withSource := true)

  val bigsaas = Project(id = "bigsaas", base = file("."), settings = defaultSettings ++ Seq(
    libraryDependencies ++= Seq(es, esclient, sprayCan, sprayRouting, sprayJson, akkaActor, akkaRemote, sprayTest, config, grizzled, guava, curator, logback)))

}
