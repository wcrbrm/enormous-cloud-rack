package cloud.enormous.rack.graphql

import cloud.enormous.rack.services.{AuthService, DummyService, ServerService}
import sangria.schema._

import scala.concurrent.ExecutionContext

case class GraphQLContextServices(
                                   authService: AuthService,
                                   dummyService: DummyService,
                                   serverService: ServerService
                                 )(implicit executionContext: ExecutionContext)

case class GraphQLContext(
                           credentials: Option[Map[String, AnyRef]],
                           services: GraphQLContextServices
                         )

object GraphqlSchemaDefinition {

  val Query = ObjectType(
    "Query", fields[GraphQLContext, Unit](
      DummyService.graphqlFields,
      ServerService.graphqlFields
    ))

  val Mutation = ObjectType(
    "Mutation", fields[GraphQLContext, Unit](
      DummyService.graphqlMutationsAddDummy
      // ServerService.graphqlMutationsAddDummy
    )
  )

  val apiSchema = Schema(Query, Some(Mutation))
}
