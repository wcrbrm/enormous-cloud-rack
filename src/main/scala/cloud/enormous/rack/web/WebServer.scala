package cloud.enormous.rack.web;

import akka.http.scaladsl.Http
import scala.util.Properties
// import scala.concurrent.{ Await, Future }
// import scala.concurrent.duration._
import com.typesafe.scalalogging.StrictLogging

object WebServer extends App with StrictLogging {

    implicit val system = akka.actor.ActorSystem("rack")
    implicit val materializer = akka.stream.ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val port: Int = Properties.envOrElse("PORT", "8080").toInt
    val binding = Http().bindAndHandle(Routes.getAll, "localhost", port)
    logger.info(s"Server online at http://localhost:${port}/, press RETURN to stop...")
}
