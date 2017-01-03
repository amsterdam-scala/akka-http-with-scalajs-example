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
    ReactDOM.render(TodoControl.component(), dom.document.getElementById("root"))
  }
}

object AppService {
  val basePath: String = dom.document.location.origin.toOption.getOrElse("")
  val client = new Client(basePath)

  def addTodo(text: String): Future[Unit] = client[Api].addTodo(text).call()

  def allTodos(): Future[Seq[TodoItem]] = client[Api].allTodos().call()

  class Client(val basePath: String) extends autowire.Client[Js.Value, Reader, Writer] {
    override def doCall(req: Request): Future[Js.Value] = {
      dom.ext.Ajax.post(
        url = s"$basePath/api/${req.path.mkString("/")}",
        data = upickle.json.write(Js.Obj(req.args.toSeq: _*))
      ).map(r => upickle.json.read(r.responseText))
    }

    def read[Result: Reader](p: Js.Value): Result = readJs[Result](p)

    def write[Result: Writer](r: Result): Js.Value = writeJs(r)
  }

}