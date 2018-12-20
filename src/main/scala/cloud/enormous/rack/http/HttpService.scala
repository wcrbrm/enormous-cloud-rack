package cloud.enormous.rack.http

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import cloud.enormous.rack.graphql.{GraphQLContextServices, GraphqlRoute}
import cloud.enormous.rack.services.AuthService
import cloud.enormous.rack.utils.Configuration

import scala.concurrent.ExecutionContext

class HttpService(graphQLContext: GraphQLContextServices)(
    implicit executionContext: ExecutionContext,
    actorSystem: ActorSystem,
    authService: AuthService) extends Configuration {

  val settings = CorsSettings.defaultSettings.copy(allowedMethods = List(GET, POST, PUT, HEAD, OPTIONS, DELETE))
  val graphqlRoute = new GraphqlRoute(graphQLContext)
  def sse: Route = path("events") { Sse.stream }

  val routes =
    cors(settings) {
      graphqlRoute.route ~ sse
    }
}
