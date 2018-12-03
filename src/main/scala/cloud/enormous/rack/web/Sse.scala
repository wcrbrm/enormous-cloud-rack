package cloud.enormous.rack.web

import akka.http.scaladsl.server.Directives._

object Sse {
    import java.time.LocalTime
    import java.time.format.DateTimeFormatter.ISO_LOCAL_TIME
    import scala.concurrent.duration._
    import akka.http.scaladsl.model.sse.ServerSentEvent
    import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

    def stream = get {
        complete {
          akka.stream.scaladsl.Source
            .tick(2.seconds, 2.seconds, akka.NotUsed)
            .map(_ => LocalTime.now())
            .map(time => ServerSentEvent(ISO_LOCAL_TIME.format(time)))
            .keepAlive(1.second, () => ServerSentEvent.heartbeat)
        }
    }
}
