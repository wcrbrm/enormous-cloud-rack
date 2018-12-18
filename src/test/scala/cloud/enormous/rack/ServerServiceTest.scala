package cloud.enormous.rack
import sangria.macros._
// import spray.json._

class ServerServiceTest extends BaseServiceTest {

    "Server API Schema" should {
      "correctly query for single server" in {
        val query = graphql"""
          query{server(id: "testserver1") {
            id
            groupId
            name
            ip
          }
        }"""
        val res = executeQuery(query)
        println(res)
      }
    }

  }



