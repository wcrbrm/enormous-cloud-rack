package cloud.enormous.rack.web

import akka.http.scaladsl.model.{ StatusCodes }
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._

// 
final case class PingPacket(result: String, date: String, ip: String)

// JSON serializers
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val pingFormat = jsonFormat3(PingPacket)
}

object Routes extends CORSHandler with JsonSupport with DefaultJsonProtocol {

    // implicit def rejectionHandler =
    // RejectionHandler.newBuilder()
    //     .handle { case MissingQueryParamRejection(param) =>
    //     val errorResponse = write(ErrorResponse(BadRequest.intValue, "Missing Parameter", s"The required $param was not found."))
    //     complete(HttpResponse(BadRequest, entity = HttpEntity(ContentTypes.`application/json`, errorResponse)))
    //     }
    // .result()

    def ip: String = {
        scala.util.Try {
            val socket = new java.net.DatagramSocket()
            socket.connect(java.net.InetAddress.getByName("8.8.8.8"), 10002)
            socket.getLocalAddress.getHostAddress
        }.getOrElse("")
    }
    val ping = path("ping") { get { 
        complete(PingPacket("pong", java.time.Instant.now.toString, ip)) 
    } }

    def sse = path("events") { Sse.stream }
    def auth = path("auth" / "check") { complete("auth ok") }
    def verify = path("auth" / "verify") { complete("verify ok") }
    def servers = path("servers") { complete("servers ok") }
    def users = path("users") { complete("users ok") }
    val api = pathPrefix("api") { corsHandler{ ping ~ sse ~ servers ~ users ~ auth } }

    val webapp = get { getFromResourceDirectory("webapp") }
    val login = redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) { 
        path("login") { getFromResource("webapp/login.html") }
    }
    val dash = redirectToNoTrailingSlashIfPresent(StatusCodes.MovedPermanently) { 
        path("dashboard") { getFromResource("webapp/index.html") }
    }
    val root = pathSingleSlash { redirect( "login", StatusCodes.MovedPermanently) }
    def getAll = api ~ login ~ dash ~ root ~ webapp
}