package cloud.enormous.rack.services

import cloud.enormous.rack.graphql.GraphQLContext
import cloud.enormous.rack.models.Server
import cloud.enormous.rack.models.db.ServerRepository
import cloud.enormous.rack.utils.Persistence
import sangria.macros.derive._
import sangria.schema.{Args, Argument, Field, InputField, InputObjectType, ListType, ObjectType, OptionInputType, OptionType, StringType, fields}

import scala.concurrent.{ExecutionContext, Future}

case class CreateServerInput(id: String, groupId: String, name: String, ip: String)
case class UpdateServerInput(groupId: Option[String], name: Option[String], ip: Option[String])

class ServerService()(implicit executionContext: ExecutionContext)  extends Persistence {
  val serverRepository = new ServerRepository()

  def findGraphQL(args : Args): Future[Seq[Server]] = {
    executeOperation {
      serverRepository.find(
        id = args.argOpt("id"),
        groupId = args.argOpt("groupId"),
        name = args.argOpt("name"),
        ip = args.argOpt("ip")
      )
    }
  }

  @GraphQLField
  def createServer(input: CreateServerInput): Future[Server] = {
    val obj = Server(Some(input.id), Some(input.groupId), Some(input.name), input.ip)
    executeOperation {
      serverRepository.save(obj)
    }
  }

//  @GraphQLField
//  def updateServer(id: String, input: UpdateServerInput): Future[Server] = {
//    executeOperation {
//      serverRepository.findById(id).flatMap( xs => {
//        xs.length match {
//          case 1 => DBIO.successful(xs.head)
//          case n => DBIO.failed(new RuntimeException(s"Expected 1 result, not $n"))
//        }
//      })
//      serverRepository.save(obj)
//    }
//  }
}

object ServerService {
  implicit val graphqlType: ObjectType[ServerService, Server] = deriveObjectType[ServerService, Server]()

  val graphqlFields: Field[GraphQLContext, Unit] = fields[GraphQLContext, Unit](
    Field("server", ListType(graphqlType),
      arguments = Argument("id", OptionInputType(StringType), description = "ID of the server")
        :: Argument("groupId", OptionInputType(StringType), description = "Group of servers")
        :: Argument("name", OptionInputType(StringType), description = "Name of the server")
        :: Argument("ip", OptionInputType(StringType), description = "IP address and port for ssh connection")
        :: Nil,
      resolve = f => f.ctx.services.serverService.findGraphQL(f.args)
    )).head

  // TODO: change into single argument input

  val CreateServerInputType = InputObjectType[CreateServerInput]("CreateServerInput", List(
    InputField("id", StringType),
    InputField("groupId", StringType),
    InputField("name", StringType),
    InputField("ip", StringType)
  ))

//  val graphqlMutationsCreateServer: Field[GraphQLContext, Unit] = fields[GraphQLContext, Unit](Field("createServer", OptionType(ServerService.graphqlType),
//    arguments = Argument("input", CreateServerInputType),
//    resolve = f => f.ctx.services.serverService.createServer(
//      f.arg("input")
//    )
//  )).head

  // TODO: graphqlMutationsUpdateServer
  // TODO: graphqlMutationsDeleteServer
}
