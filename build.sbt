import com.typesafe.sbt.SbtMultiJvm
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm
 
val akkaVersion = "2.5.17"
val akkaHttpVersion = "10.1.5"

lazy val `rack` = project
  .in(file("."))
  .settings(SbtMultiJvm.multiJvmSettings: _*)
  .enablePlugins(JavaAppPackaging)
  .settings(
    organization := "cloud.enormous.rack",
    scalaVersion := "2.12.6",
    scalacOptions in Compile ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlog-reflective-calls", "-Xlint"),
    javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
    javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",

      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-remote" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
      "com.typesafe.akka" %% "akka-multi-node-testkit" % akkaVersion,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "io.kamon" % "sigar-loader" % "1.6.6-rev002"
    ),

    fork in run := true,
    parallelExecution in Test := false,
    mainClass in (Compile, run) := Some("cloud.enormous.rack.web.WebServer")
  ).configs(MultiJvm)
