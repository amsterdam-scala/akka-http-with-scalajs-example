package example

import autowire._
import japgolly.scalajs.react.ReactDOM
import org.scalajs.dom
import upickle.Js
import upickle.default.{Reader, Writer, readJs, writeJs}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSApp

object App extends JSApp {
  /**
    * Main entry point
    */
  def main(): Unit = {
  }

  val client = new Client(dom.document.location.origin.toOption.getOrElse(""))

  def addTodo(taskWithoutId: String): Future[Task0] =
    client[Api].createTodo(Task0(None, taskWithoutId, done = false)).call()

  def allTodos(): Future[Iterable[Task0]] = client[Api].allTodo0().call()

  class Client(val basePath: String) extends autowire.Client[Js.Value, Reader, Writer] {
     def doCall(req: Request): Future[Js.Value] = dom.ext.Ajax.post(
      url = s"$basePath/api/${req.path.mkString("/")}",
      data = upickle.json.write(Js.Obj(req.args.toSeq: _*))
    ).map(r => upickle.json.read(r.responseText))

    def read[Result: Reader](p: Js.Value): Result = readJs[Result](p)

    def write[Result: Writer](r: Result): Js.Value = writeJs(r)
  }

}