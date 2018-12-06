package cloud.enormous.rack.web;

import org.scalatest.{ WordSpec, MustMatchers }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._

class RoutesSpec extends WordSpec with MustMatchers with ScalatestRouteTest {

    "The service" should  {
        "return PONG in response to PING" in {
            Get("/api/ping") ~> Routes.getAll ~> check {
                status must equal(StatusCodes.OK)
                responseAs[String] must include("pong")
                responseAs[String] must include("ip")
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