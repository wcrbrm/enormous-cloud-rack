package cloud.enormous.rack.http

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import cloud.enormous.rack.graphql.{GraphQLContextServices, GraphqlRoute}
import cloud.enormous.rack.services.{AuthService, DummyService}
import cloud.enormous.rack.utils.Configuration

import scala.concurrent.ExecutionContext

class HttpService(graphQLContext: GraphQLContextServices)(implicit executionContext: ExecutionContext, actorSystem: ActorSystem, authService: AuthService) extends Configuration {

  val settings = CorsSettings.defaultSettings.copy(allowedMethods = List(GET, POST, PUT, HEAD, OPTIONS, DELETE))
  val graphqlRoute = new GraphqlRoute(graphQLContext)
  val routes =
    cors(settings) {
      graphqlRoute.route
    }
}
