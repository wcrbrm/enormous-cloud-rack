package cloud.enormous.rack
import sangria.macros._
import spray.json._

class ServerServiceTest extends BaseServiceTest {

    def getById(id: String): JsValue = {
      val query = graphql"""query {
          server(id: "testserver1") { id groupId name ip }
      }"""
      executeQuery(query)
    }

    "Server API Schema" should {

      "correctly find server by ID" in {
        getById("testserver1") should be (
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
/*
      "correctly mutate: CREATE, READ, UPDATE, READ, DELETE, READ" in {

        // 1. CREATE
        val queryCreate = graphql"""mutation {
          createServer(id: "testCRUD", groupId: "", name: "Test CRUD", ip: "127.0.0.1") {
            id groupId name ip
          }
        }"""
        println(executeQuery(queryCreate))
        println(getById("testCRUD"))


        // 2. TODO: READ AFTER CREATION
        // 3. UPDATE
//        val queryUpdate = graphql"""mutation {
//          updateServer(id: "testCRUD", groupId: "Another", name: "Test CRUD 1", ip: "127.0.0.1:2222") {
//            id groupId name ip
//          }
//        }"""
//        println(executeQuery(queryUpdate))

        // 4. TODO: READ AFTER UPDATE
        // 5. DELETE
//        val queryDelete = graphql"""mutation {
//          deleteServer(id: "testCRUD") { id }
//        }"""
//        println(executeQuery(queryDelete))

        // 6. TODO: READ AFTER DELETE, MUST BE MISSING
      }
*/
    }

  }



