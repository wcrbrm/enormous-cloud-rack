package cloud.enormous.rack.web
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

// class that can be serialized to JSON
final case class PingPacket(result: String, time: Long, ip: String)

// protocol for serialization, please import it for (un-)marchalling
object PingJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val pingFormat = jsonFormat3(PingPacket)
}

object Ping {

    import PingJsonProtocol._

    // exposing real external IP address of the machine, serving that API
    def ip: String = {
        scala.util.Try {
            val socket = new java.net.DatagramSocket()
            socket.connect(java.net.InetAddress.getByName("8.8.8.8"), 10002)
            socket.getLocalAddress.getHostAddress
        }.getOrElse("")
    }
    val route = path("ping") { get { 
        complete(PingPacket("pong", new java.util.Date().getTime / 1000, ip)) 
    } }
}
