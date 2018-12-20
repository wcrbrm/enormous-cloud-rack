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
      DummyService.graphqlMutationsAddDummy,
      // server mutation:
      ServerService.graphqlMutationCreateServer,
      ServerService.graphqlMutationUpdateServer,
      ServerService.graphqlMutationDeleteServer
    )
  )

  val Types: List[Type with Named] = List(
  )

  val apiSchema = Schema(
    query = Query,
    mutation = Some(Mutation),
    subscription = None,
    additionalTypes = Types
  )
}
