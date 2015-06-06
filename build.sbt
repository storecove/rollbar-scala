organization := "com.github.acidghost"

name := "rollbar-scala"

version := "1.0"

scalaVersion := "2.11.6"

val slf4jVersion = "1.7.12"
val dispatchVersion = "0.11.2"
val log4jVersion = "1.2.17"
val logbackVersion = "1.1.3"
val json4sVersion = "3.2.11"
val scalatestVersion = "2.2.4"

libraryDependencies ++= Seq(
    "org.slf4j" % "slf4j-api" % slf4jVersion % "provided",
    "net.databinder.dispatch" %% "dispatch-core" % dispatchVersion,
    "log4j" % "log4j" % log4jVersion % "provided",
    "ch.qos.logback" % "logback-classic" % logbackVersion % "provided",
    "org.json4s" %% "json4s-jackson" % json4sVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test"
)

// Plugins configuration
net.virtualvoid.sbt.graph.Plugin.graphSettings
