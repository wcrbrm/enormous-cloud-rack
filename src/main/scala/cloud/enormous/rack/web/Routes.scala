package cloud.enormous.rack.web;

import akka.http.scaladsl.model.{ StatusCodes }
import akka.http.scaladsl.server.Directives._

object Sse {
    import java.time.LocalTime
    import java.time.format.DateTimeFormatter.ISO_LOCAL_TIME
    import scala.concurrent.duration._
    import akka.http.scaladsl.model.sse.ServerSentEvent
    import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

    def stream = get {
        complete {
          akka.stream.scaladsl.Source
            .tick(2.seconds, 2.seconds, akka.NotUsed)
            .map(_ => LocalTime.now())
            .map(time => ServerSentEvent(ISO_LOCAL_TIME.format(time)))
            .keepAlive(1.second, () => ServerSentEvent.heartbeat)
        }
    }
}

object Routes {

    val ping = path("ping") { complete("pong")  }
    def sse = path("events") { Sse.stream }
    def auth = path("auth" / "check") { complete("auth ok") }
    def verify = path("auth" / "verify") { complete("verify ok") }
    def servers = path("servers") { complete("servers ok") }
    def users = path("users") { complete("users ok") }
    val api = pathPrefix("api") { ping ~ sse ~ servers ~ users ~ auth }

    val webapp = get { getFromResourceDirectory("webapp") }
    val login = redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) { 
        path("login") { getFromResource("webapp/login.html") }
    }
    val dash = redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) { 
        path("dashboard") { getFromResource("webapp/index.html") }
    }
    val root = pathSingleSlash { redirect( "login", StatusCodes.MovedPermanently) }
    def merged = api ~ login ~ dash ~ root ~ webapp

}