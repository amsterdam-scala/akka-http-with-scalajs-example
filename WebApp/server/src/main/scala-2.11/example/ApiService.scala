package example

import java.time.LocalDateTime

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import upickle.Js
import upickle.Js.Value
import upickle.default._

trait ApiService {
  this: ServiceContext =>
  lazy val routeApi: Route =
    path(Segments) { s =>
      entity(as[String]) { e =>
        complete {
          AutowireServer.route[Api](ApiImpl)(
            autowire.Core.Request(s, upickle.json.read(e).asInstanceOf[Js.Obj].value.toMap)
          ).map(upickle.json.write(_, 0))
        }
      }
    }

}

object ApiImpl extends Api {
  var data: Seq[TodoItem0] = Seq.empty

  def addTodo(text: String): Unit = {
    println(s"addTodo: $text")
    data = data :+ TodoItem0(text, LocalDateTime.now().toLocalTime.toString)
  }

  def allTodos(): Seq[TodoItem0] = {
    println("allTodos")
    data
  }
}

object AutowireServer extends autowire.Server[Js.Value, Reader, Writer] {
  def read[Result: Reader](p: Js.Value): Result = upickle.default.readJs[Result](p)

  def write[Result: Writer](r: Result): Value = upickle.default.writeJs(r)
}
