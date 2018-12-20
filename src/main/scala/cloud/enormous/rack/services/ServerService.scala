package cloud.enormous.rack.services

import cloud.enormous.rack.graphql.GraphQLContext
import cloud.enormous.rack.models.Server
import cloud.enormous.rack.models.db.ServerRepository
import cloud.enormous.rack.utils.Persistence
import sangria.macros.derive._
import spray.json._
import sangria.marshalling.sprayJson._
import sangria.schema.{Args, Argument, Field, InputField, InputObjectType, ListType, ObjectType, OptionInputType, OptionType, StringType, fields}
import scala.concurrent.{ExecutionContext, Future}

case class CreateServerInput(id: String, groupId: Option[String], name: String, ip: String)
case class UpdateServerInput(id: String, groupId: Option[String], name: Option[String], ip: Option[String])


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
    val obj = Server(Some(input.id), input.groupId, Some(input.name), input.ip)
    println(s"creatingServer ${obj}")
    executeOperation {
      serverRepository.save(obj)
    }
  }

  @GraphQLField
  def updateServer(id: String, input: UpdateServerInput): Future[String] = {
    println(s"updateServer ${id}: ${input}")
    Future { "ok" }
  }

  @GraphQLField
  def deleteServer(id: String): Future[String] = {
    println(s"deleteServer ${id}")
    Future { "ok" }
  }
}

object ServerService {
  implicit val graphqlServerType: ObjectType[ServerService, Server] = deriveObjectType[ServerService, Server]()
  // implicit val graphqlIntType: ObjectType[ServerService, Int] = deriveObjectType[ServerService, Int]()

  val graphqlFields: Field[GraphQLContext, Unit] = fields[GraphQLContext, Unit](
    Field("server", ListType(graphqlServerType),
      arguments = Argument("id", OptionInputType(StringType), description = "ID of the server")
        :: Argument("groupId", OptionInputType(StringType), description = "Group of servers")
        :: Argument("name", OptionInputType(StringType), description = "Name of the server")
        :: Argument("ip", OptionInputType(StringType), description = "IP address and port for ssh connection")
        :: Nil,
      resolve = f => f.ctx.services.serverService.findGraphQL(f.args)
    )).head

  val CreateServerInputType = InputObjectType[CreateServerInput]("CreateServerInput", List(
    InputField("id", StringType),
    InputField("groupId", StringType),
    InputField("name", StringType),
    InputField("ip", StringType)
  ))

  val UpdateServerInputType = InputObjectType[CreateServerInput]("UpdateServerInput", List(
    InputField("id", StringType),
    InputField("groupId", OptionInputType(StringType)),
    InputField("name", OptionInputType(StringType)),
    InputField("ip", OptionInputType(StringType)),
  ))

  object JsonProtocol extends DefaultJsonProtocol {
    implicit val formatCreateServerInput = jsonFormat4(CreateServerInput.apply)
    implicit val formatUpdateServerInput = jsonFormat4(UpdateServerInput.apply)
  }
  import JsonProtocol._

  val graphqlMutationCreateServer = fields[GraphQLContext, Unit](Field("createServer", OptionType(graphqlServerType),
    arguments = Argument("input", CreateServerInputType) :: Nil,
    resolve = f => f.ctx.services.serverService.createServer(f.arg("input"))
  )).head
  val graphqlMutationUpdateServer = fields[GraphQLContext, Unit](Field("updateServer", OptionType(StringType),
    arguments = Argument("id", StringType ) :: Argument("input", UpdateServerInputType) :: Nil,
    resolve = f => f.ctx.services.serverService.updateServer(f.arg("id"), f.arg("input"))
  )).head
  val graphqlMutationDeleteServer = fields[GraphQLContext, Unit](Field("deleteServer", OptionType(StringType),
    arguments = Argument("id", StringType) :: Nil,
    resolve = f => f.ctx.services.serverService.deleteServer(f.arg("id"))
  )).head

}
