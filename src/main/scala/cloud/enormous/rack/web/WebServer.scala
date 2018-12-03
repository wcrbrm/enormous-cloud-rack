package cloud.enormous.rack.web;

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ StatusCodes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import scala.util.Properties
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging

object WebServer extends App with StrictLogging {

    implicit val system = akka.actor.ActorSystem("rack")
    implicit val materializer = akka.stream.ActorMaterializer()
    implicit val executionContext = system.dispatcher

    // sse, routeServers, routeAuth, routeUsers
    val ping = pathPrefix("api") { path("ping") { complete("PONG")  } }
    val webapp = get { getFromResourceDirectory("webapp") }
    val login = redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) { 
        path("login") { getFromResource("webapp/login.html") }
    }
    val dash = redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) { 
        path("dashboard") { getFromResource("webapp/index.html"); }
    }
    val root = pathSingleSlash { redirect( "login", StatusCodes.MovedPermanently) }
    val route = ping ~ login ~ dash ~ root ~ webapp

    val port: Int = Properties.envOrElse("PORT", "8080").toInt
    val binding: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", port)
    logger.info(s"Server online at http://localhost:${port}/, press RETURN to stop...")
}
