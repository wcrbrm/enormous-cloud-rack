package cloud.enormous.rack.services

import cloud.enormous.rack.graphql.GraphQLContext
import cloud.enormous.rack.models.Server
import cloud.enormous.rack.models.db.ServerRepository
import cloud.enormous.rack.utils.Persistence
import sangria.macros.derive._
import sangria.schema.{Args, Argument, Field, ListType, OptionInputType, OptionType, StringType, fields}

import scala.concurrent.ExecutionContext

class ServerService()(implicit executionContext: ExecutionContext)  extends Persistence {
  val serverRepository = new ServerRepository()

  def findGraphQL(args : Args) = {
    executeOperation {
      serverRepository.find(
        id = args.argOpt("id"),
        groupId = args.argOpt("groupId"),
        name = args.argOpt("name"),
        ip = args.argOpt("ip")
      )
    }
  }
}

object ServerService {
  implicit val graphqlType = deriveObjectType[ServerService, Server]()

  val graphqlFields = fields[GraphQLContext, Unit](
    Field("name", ListType(graphqlType),
      arguments = Argument("id", OptionInputType(StringType), description = "ID of the server")
        :: Argument("groupId", OptionInputType(StringType), description = "Group of servers")
        :: Argument("name", OptionInputType(StringType), description = "Name of the server")
        :: Argument("ip", OptionInputType(StringType), description = "IP address and port for ssh connection")
        :: Nil,
      resolve = f => f.ctx.services.serverService.findGraphQL(f.args)
    )).head

//  val graphqlMutationsAddDummy = fields[GraphQLContext, Unit](Field("addDummy", OptionType(DummyService.graphqlType),
//    arguments = Argument("dummy", StringType) :: Nil,
//    resolve = f => f.ctx.services.dummyService.addDummy(f.arg("dummy"))
//  )).head

}
