package cloud.enormous.rack

import akka.http.scaladsl.testkit.ScalatestRouteTest
import cloud.enormous.rack.graphql.GraphqlSchemaDefinition.apiSchema
import cloud.enormous.rack.graphql.{GraphQLContext, GraphQLContextServices}
import cloud.enormous.rack.http.HttpService
import cloud.enormous.rack.services.{AuthService, DummyService, ServerService}
import cloud.enormous.rack.utils.AutoValidate
import cloud.enormous.rack.utils.InMemoryPostgresStorage._
import org.scalatest.{Matchers, WordSpec}
import sangria.ast.Document
import sangria.execution.Executor
import sangria.marshalling.sprayJson._
// import sangria.renderer.SchemaRenderer
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseServiceTest extends WordSpec with Matchers with ScalatestRouteTest {

  // println(SchemaRenderer.renderSchema(apiSchema))
  dbProcess.getProcessId

  implicit val authService = new AuthService(new AutoValidate)

  val dummyService = new DummyService()
  val serverService = new ServerService()

  val graphQLContextServices = GraphQLContextServices(authService, dummyService, serverService)

  val httpService = new HttpService(graphQLContextServices)

  def executeQuery(query: Document, vars: JsObject = JsObject.empty) = {
    val futureResult = Executor.execute(apiSchema, query,
      variables = vars,
      userContext = GraphQLContext(None, graphQLContextServices)
    )
    Await.result(futureResult, 5.seconds)
  }
}
