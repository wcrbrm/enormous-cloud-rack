package cloud.enormous.rack

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import cloud.enormous.rack.http.HttpService
import cloud.enormous.rack.graphql.GraphQLContextServices
import cloud.enormous.rack.services.{AuthService, DummyService, ServerService}
import cloud.enormous.rack.utils.{AWSCognitoValidation, AutoValidate, Configuration, FlywayService}

import scala.concurrent.ExecutionContext

object Main extends App with Configuration {
  // $COVERAGE-OFF$Main Application Wrapper
  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val log: LoggingAdapter = Logging(actorSystem, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val flywayService = new FlywayService(jdbcUrl, dbUser, dbPassword)
  flywayService.migrateDatabaseSchema

  implicit val authService = new AuthService(new AutoValidate)
  //implicit val authService = new AuthService(new AWSCognitoValidation(authCognito, log)) Use this Service for AWS ;-)

  val dummyService = new DummyService()
  val serverService  = new ServerService()
  val graphQLContextServices = GraphQLContextServices(authService, dummyService, serverService)
  val httpService = new HttpService(graphQLContextServices)
  Http().bindAndHandle(httpService.routes, httpHost, httpPort)
  // $COVERAGE-ON$
}
