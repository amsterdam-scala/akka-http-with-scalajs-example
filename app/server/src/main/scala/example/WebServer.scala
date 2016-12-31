package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object WebServer extends App with Routing {
  implicit val system = ActorSystem()
  implicit val (executor, materializer) = (system.dispatcher, ActorMaterializer())

  val config = ConfigFactory.load()
  val (interface, port) = (config.getString("http.interface"), config.getInt("http.port"))

  val bindingFuture = Http().bindAndHandle(root, interface, port)

  println(s"Server online at http://$interface:$port")
}
