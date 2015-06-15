organization := "com.storecove"

name := "rollbar-scala"

version := "1.0"

description := "Notifier library for integrating Scala apps with the Rollbar service."

scalaVersion := "2.10.4"

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

publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
    else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra in Global := {
    <url>https://github.com/storecove/rollbar-scala</url>
    <licenses>
        <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
    </licenses>
    <scm>
        <connection>scm:git@github.com:storecove/rollbar-scala.git</connection>
        <developerConnection>scm:git:git@github.com:storecove/rollbar-scala.git</developerConnection>
        <url>git@github.com:storecove/rollbar-scala</url>
    </scm>
    <developers>
        <developer>
            <id>acidghost</id>
            <name>Andrea Jemmett</name>
            <url>https://github.com/acidghost</url>
            <organization>Storecove</organization>
            <organizationUrl>http://www.storecove.com</organizationUrl>
        </developer>
    </developers>
}

net.virtualvoid.sbt.graph.Plugin.graphSettings
