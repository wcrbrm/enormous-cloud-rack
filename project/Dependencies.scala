import sbt._

object Version {
  final val akka = "2.5.17"
  final val akkaHttp = "10.1.5"
  final val akkaHttpSprayJson =  "10.1.5"
  final val Scala = "2.12.6"
  final val AkkaLog4j = "1.4.0"
  final val Log4j = "2.8.2"
  final val swagger = "1.5.14"
  final val swaggerAkka = "0.9.2"
  final val akkaHttpCors = "0.2.1"
  final val slickRepo = "1.5.2"
  final val postgresql = "9.4-1206-jdbc42"
  final val nimbusds = "4.23"
  final val slick = "3.2.3"
  final val slickPg = "0.17.0"
  final val flyway = "3.2.1"
  final val sangriaSprayJson = "1.0.1"
  final val sangria = "1.4.2"
}

object Library {

  val swagger = "io.swagger" % "swagger-jaxrs" % Version.swagger
  val swaggerAkka = "com.github.swagger-akka-http" %% "swagger-akka-http" % Version.swaggerAkka

  val akkaHttp = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.akka
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % Version.akka
  val akkaHttpCors = "ch.megard" %% "akka-http-cors" % Version.akkaHttpCors
  val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttpSprayJson

  val log4jCore = "org.apache.logging.log4j" % "log4j-core" % Version.Log4j
  val slf4jLog4jBridge = "org.apache.logging.log4j" % "log4j-slf4j-impl" % Version.Log4j
  val akkaLog4j = "de.heikoseeberger" %% "akka-log4j" % Version.AkkaLog4j

  val slick = "com.typesafe.slick" %% "slick" % Version.slick
  val slickPg = "com.github.tminglei" %% "slick-pg" % Version.slickPg
  val slickPgSprayJson = "com.github.tminglei" %% "slick-pg_spray-json" % Version.slickPg

  val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % Version.slick
  val postgresql =  "org.postgresql" % "postgresql"% Version.postgresql
  val slickRepo =  "com.byteslounge" %% "slick-repo" % Version.slickRepo
  val flywaydb = "org.flywaydb" % "flyway-core" % Version.flyway

  val nimbusds = "com.nimbusds" % "nimbus-jose-jwt" % Version.nimbusds

  val sangriaSprayJson = "org.sangria-graphql" %% "sangria-spray-json" % Version.sangriaSprayJson
  val sangria = "org.sangria-graphql" %% "sangria" % Version.sangria
  val sangriaRelay = "org.sangria-graphql" %% "sangria-relay" % Version.sangria


}

object TestVersion {
  final val akkaTestkit = "2.5.3"
  final val akkaHttpTestkit =  "10.0.9"
  final val postgresqlEmbedded = "2.2"
  final val scalaTest = "3.0.1"
}

object TestLibrary {
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % TestVersion.akkaTestkit % "test"
  val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % TestVersion.akkaHttpTestkit % "test"
  val postgresqlEmbedded = "ru.yandex.qatools.embed" % "postgresql-embedded" % TestVersion.postgresqlEmbedded % "test"
  val scalaTest = "org.scalatest" %% "scalatest" % TestVersion.scalaTest % "test"
}
