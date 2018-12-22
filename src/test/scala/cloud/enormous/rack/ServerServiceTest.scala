package cloud.enormous.rack
import sangria.ast.Document
import sangria.macros._
import sangria.parser.QueryParser
import spray.json._
import org.scalatest._

class ServerServiceTest extends BaseServiceTest {

    def getById(id: String): JsValue = {
      val document: Document = QueryParser.parse(
          s"""query { server(id: "$id") { id groupId name ip } }"""
      ).get
      executeQuery(document)
    }

    "Server API Schema" should {

      "correctly find server by ID" in {
        val testserverJson = s"""{"id": "testserver1", "groupId": "production", "name": "Production Server", "ip": "127.0.0.1:2200" }"""
        getById("testserver1") should be
          s""" {"data":{"server":$testserverJson}} """.parseJson
      }

      // find server by multiple IDS
      // find server by search queries

      "not update something absent" in {
        val queryUpdate = graphql"""mutation {
          updateServer(id: "testBadId", input: {groupId: "newGroup", name: "Test CRUD2", ip: "127.0.0.1:2299" }) {
            id groupId name ip
          }
        }"""
        executeQuery(queryUpdate) should be (s""" {"data":{"updateServer":null}} """.parseJson)
      }

      "correctly mutate: CREATE, READ, UPDATE, READ, DELETE, READ" in {
        // 1. CREATE and vertify
        val id = "testCRUD"
        val serverCreatedJson = s"""{"id": "$id", "groupId": "", "name": "Test CRUD", "ip": "127.0.0.1" }"""

        val queryCreate = graphql"""mutation {
          createServer(input: {id: "testCRUD", groupId: "", name: "Test CRUD", ip: "127.0.0.1" }) {
            id groupId name ip
          }
        }"""
        executeQuery(queryCreate) should be (s""" {"data":{"createServer":$serverCreatedJson}} """.parseJson)
        getById(id) should be (s""" {"data":{"server":[$serverCreatedJson]}} """.parseJson)

        // 2. UPDATE and verify
        val serverUpdatedJson = s"""{"id": "$id", "groupId": "newGroup", "name": "Test CRUD2", "ip": "127.0.0.1:2299" }"""
        val queryUpdate = graphql"""mutation {
          updateServer(id: "testCRUD", input: {groupId: "newGroup", name: "Test CRUD2", ip: "127.0.0.1:2299" }) {
            id groupId name ip
          }
        }"""
        executeQuery(queryUpdate) should be (s""" {"data":{"updateServer":$serverUpdatedJson}} """.parseJson)
        getById(id) should be (s""" {"data":{"server":[$serverUpdatedJson]}} """.parseJson)

        // 3. DELETE and verify
        val serverDeleteJson = s"""{"id": "$id"}"""
        val queryDelete = graphql"""mutation {
          deleteServer(id: "testCRUD") { id }
        }"""
        executeQuery(queryDelete) should be (s""" {"data":{"deleteServer":$serverDeleteJson}} """.parseJson)
        getById(id) should be (s""" {"data":{"server":[]}} """.parseJson)
      }

    }

  }



