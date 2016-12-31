package example

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import shared.SharedMessages

trait Routing extends Directives {
  val root: Route =
    pathSingleSlash {
      get {
        complete(example.html.index.render(SharedMessages.itWorks))
      }
    } ~
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      }
}
