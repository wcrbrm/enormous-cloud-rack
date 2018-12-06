package cloud.enormous.rack.web

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{ JsObject, JsString, JsValue }
import scala.concurrent.ExecutionContext
import scala.util.{ Success, Failure }
import sangria.parser.QueryParser
import sangria.ast.Document
import sangria.execution._
import cloud.enormous.rack.schema.GraphQLSchema

object GraphQLServer {

  private def executeGraphQLQuery(
    query: Document, 
    operation: Option[String], 
    vars: JsObject
  )(implicit ec: ExecutionContext) = {

    Executor.execute(
      GraphQLSchema.SchemaDefinition, 
      query, 
      MyContext(dao), // REPOSITORY
      variables = vars, 
      operationName = operation
    ).map(OK -> _)
      .recover {
      case error: QueryAnalysisError => BadRequest -> error.resolveError
      case error: ErrorWithResolver => InternalServerError -> error.resolveError
    }
  }

  def graphQLEndpoint(requestJson: JsValue)(implicit ec: ExecutionContext): Route = {

      val JsObject(fields) = requestJson
      val JsString(query) = fields("query")
      val operation = fields.get("operationName") collect {
          case JsString(op) => op
      }
      val vars = fields.get("variables") match {
          case Some(obj: JsObject) => obj
          case _ => JsObject.empty
      }

      QueryParser.parse(query) match {
          // query parsed successfully, time to execute it!
          case Success(queryAst) =>
              complete(executeGraphQLQuery(queryAst, operation, vars))
          // can't parse GraphQL query, return error
          case Failure(error) =>
              complete(BadRequest, JsObject("error" -> JsString(error.getMessage)))
      }
  }

  def route = (post & path("graphql")) {
    entity(as[JsValue]) { requestJson ⇒
      graphQLEndpoint(requestJson)
    }
  } 
}