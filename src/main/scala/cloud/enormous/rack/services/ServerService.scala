package cloud.enormous.rack.services

import cloud.enormous.rack.graphql.GraphQLContext
import cloud.enormous.rack.models._
import cloud.enormous.rack.models.db.{MaybeFilter, ServerRepository}
import cloud.enormous.rack.utils.Persistence
import sangria.macros.derive._
import spray.json._
import sangria.marshalling.sprayJson._
import sangria.schema.{Args, Argument, Field, InputField, InputObjectType, ListType, ObjectType, OptionInputType, OptionType, StringType, fields}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}


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
    val obj = Server(Some(input.id), input.groupId, input.name, input.ip)
    executeOperation {
      serverRepository.save(obj)
    }
  }

  @GraphQLField
  def updateServer(id: String, input: UpdateServerInput): Future[Server] = {
    executeAction(serverRepository.findOne(id)) match {
      case Some(found) =>
        val updated = found.updatedWith(input)
        executeOperation(serverRepository.update(updated))
      // there might be a better version for error handling. with error message passed to GraphQL
      case None => Future { null }
    }
  }

  @GraphQLField
  def deleteServer(id: String): Future[ResultObject] = {
    executeAction(serverRepository.findOne(id)) match {
      case Some(found) =>
        executeAction(serverRepository.delete(found))
        Future { ResultObject(id) }
      case None => Future { ResultObject(id, "failure") }
    }
  }
}

object ServerService {
  implicit val graphqlServerType: ObjectType[ServerService, Server] = deriveObjectType[ServerService, Server]()
  implicit val graphqlResultType: ObjectType[ServerService, ResultObject] = deriveObjectType[ServerService, ResultObject]()

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

  val UpdateServerInputType = InputObjectType[UpdateServerInput]("UpdateServerInput", List(
    InputField("groupId", OptionInputType(StringType)),
    InputField("name", OptionInputType(StringType)),
    InputField("ip", OptionInputType(StringType)),
  ))

  object ServerJsonProtocol extends DefaultJsonProtocol {
    implicit val formatCreateServerInput = jsonFormat4(CreateServerInput.apply)
    implicit val formatUpdateServerInput = jsonFormat3(UpdateServerInput.apply)
  }
  import ServerJsonProtocol._

  val graphqlMutationCreateServer = fields[GraphQLContext, Unit](Field("createServer", OptionType(graphqlServerType),
    arguments = Argument("input", CreateServerInputType) :: Nil,
    resolve = f => f.ctx.services.serverService.createServer(f.arg("input"))
  )).head
  val graphqlMutationUpdateServer = fields[GraphQLContext, Unit](Field("updateServer", OptionType(graphqlServerType),
    arguments = Argument("id", StringType) :: Argument("input", UpdateServerInputType) :: Nil,
    resolve = f => f.ctx.services.serverService.updateServer(f.arg("id"), f.arg("input"))
  )).head
  val graphqlMutationDeleteServer = fields[GraphQLContext, Unit](Field("deleteServer", OptionType(graphqlResultType),
    arguments = Argument("id", StringType) :: Nil,
    resolve = f => f.ctx.services.serverService.deleteServer(f.arg("id"))
  )).head
}