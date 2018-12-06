package cloud.enormous.rack.web;

import org.scalatest.{ WordSpec, Matchers, MustMatchers }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import Matchers._
import spray.json._
import DefaultJsonProtocol._ 

class RoutesSpec extends WordSpec with MustMatchers with ScalatestRouteTest {

    "The service" should  {
        "return PONG in response to PING" in {
            Get("/api/ping") ~> Routes.getAll ~> check {
                status must equal(StatusCodes.OK)
                val body: String = responseAs[String]
                body must include("pong")
                body must include("ip")
                
                import PingJsonProtocol._
                val packet: PingPacket = body.parseJson.convertTo[PingPacket]
                packet.result must equal ("pong")
                packet.time should be >= 0L
                packet.ip should fullyMatch regex "^\\d+\\.\\d+\\.\\d+\\.\\d+$"
            }
        }
        "reject in response to invalid route" in {
            Get("/api/broken-link") ~> Routes.getAll ~> check {
                // println("rejection:" + rejection)
                // rejection must equal(List())
                // status must equal(StatusCodes.OK)
                // responseAs[String] must equal("pong")
            }
        }
    }
}