package cloud.enormous.rack.web;

import akka.http.scaladsl.Http
// import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import scala.util.Properties
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging

object WebServer extends App with StrictLogging {

    implicit val system = akka.actor.ActorSystem()
    implicit val materializer = akka.stream.ActorMaterializer()
    implicit val executionContext = system.dispatcher

    // assets, sse, routeServers, routeAuth, routeUsers
    var route = get {
       pathSingleSlash {
           logger.info("Requesting /")
           complete("Wow, it even works...")
       }
    }

    val port: Int = Properties.envOrElse("PORT", "8080").toInt
    val binding: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", port)
    logger.info(s"Server online at http://localhost:${port}/, press RETURN to stop...")
}
