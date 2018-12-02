import $ivy.`com.typesafe.akka::akka-http:10.1.5`
import $ivy.`com.typesafe.akka::akka-actor:2.5.18`
import $ivy.`com.typesafe.akka::akka-stream:2.5.18`

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.unmarshalling.sse.EventStreamUnmarshalling._
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.stream.scaladsl.Source

implicit val system = akka.actor.ActorSystem("sse-client")
implicit val mat = akka.stream.ActorMaterializer()
import system.dispatcher

Http()
  .singleRequest(HttpRequest(uri = "http://localhost:8080/events"))
  .flatMap(Unmarshal(_).to[Source[ServerSentEvent, akka.NotUsed]])
  .foreach(_.runForeach(println))
