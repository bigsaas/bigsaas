
import sbt._
import Defaults._

resolvers += "TypeSafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  sbtPluginExtra(
    m = "play" % "sbt-plugin" % "2.1-RC1",
    sbtV = "0.12",
    scalaV = "2.9.2"))
  
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0")

addSbtPlugin("com.github.retronym" % "sbt-onejar" % "0.8")

