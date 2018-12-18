package cloud.enormous.rack
import sangria.macros._
import spray.json._

class ServerServiceTest extends BaseServiceTest {

    "Server API Schema" should {

      "correctly find server by ID" in {
        val query = graphql"""query {
          server(id: "testserver1") { id groupId name ip }
        }"""
        executeQuery(query) should be (
          """
            {
              "data": {
                "server": [{
                  "id": "testserver1",
                  "groupId": "production",
                  "name": "Production Server",
                  "ip": "127.0.0.1:2200"
                }]
              }
            }
          """.parseJson
        )
      }
    }

  }



