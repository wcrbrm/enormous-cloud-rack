package cloud.enormous.rack.web;

import akka.http.scaladsl.model.{ StatusCodes, HttpEntity }
import akka.http.scaladsl.server.Directives._

object Routes {

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
    def merged = ping ~ login ~ dash ~ root ~ webapp

}