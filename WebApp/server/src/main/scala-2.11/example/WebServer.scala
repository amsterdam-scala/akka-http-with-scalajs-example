package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer

trait Routing extends Directives with ApiService with ServiceContext {
  val root: Route =
    get {
      pathSingleSlash {
        redirect("en/index.html", StatusCodes.MovedPermanently)
      } ~
        path("hello") { pathEnd
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http!</h1>"))
        } /*just to check */ ~
        pathPrefix("en" / Remaining) {
          case "todos" => encodeResponse(getFromResource("public/en/todo.html"))
          case file:String  =>
          // optionally compresses the response with Gzip or Deflate
          // if the client accepts compressed responses
          encodeResponse(getFromResource("public/en/" + file))
        } ~
        pathPrefix("css" / """.+\.css$""".r) { file =>
          // optionally compresses the response with Gzip or Deflate
          // if the client accepts compressed responses
          encodeResponse(getFromResource("public/css/" + file))
        } ~
        pathPrefix("js" / """.+\.js$""".r) { file =>
          // optionally compresses the response with Gzip or Deflate
          // if the client accepts compressed responses
          encodeResponse(getFromResource("public/" + file))
        } ~
        pathPrefix("img" / Remaining) { file =>
          // optionally compresses the response with Gzip or Deflate
          // if the client accepts compressed responses
          encodeResponse(getFromResource("public/img/" + file))
        }
    } ~ post {
      pathPrefix("api")(routeApi)
    }
}

object WebServer extends App with Routing {
  implicit val system = ActorSystem()
  implicit val (executor, materializer) = (system.dispatcher, ActorMaterializer())

  val config = com.typesafe.config.ConfigFactory.load()
  val (interface, port) = (config.getString("http.interface"), config.getInt("http.port"))

  val bindingFuture = Http().bindAndHandle(root, interface, port)

  println(s"Server online at http://$interface:$port")
}
