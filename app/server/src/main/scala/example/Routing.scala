package example

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}

trait Routing extends Directives {
  val root: Route =

    get {
      pathSingleSlash {
        redirect("en/index.html", StatusCodes.MovedPermanently)
    } ~
        path("hello") {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http!</h1>"))
        } /*just to check */ ~
        pathPrefix("en" / Remaining) { file =>
          // optionally compresses the response with Gzip or Deflate
          // if the client accepts compressed responses
          encodeResponse(getFromResource("public/en/" + file))
        } ~
        pathPrefix("js" / Remaining) { file =>
          // optionally compresses the response with Gzip or Deflate
          // if the client accepts compressed responses
          encodeResponse(getFromResource("public/" + file))
        }
    }
}