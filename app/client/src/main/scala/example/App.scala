package example

import japgolly.scalajs.react.ReactDOM
import org.scalajs.dom
import org.scalajs.dom._
import example.Model.TodoItem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js.JSApp
import upickle.Js
import upickle.default._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.JSApp

import autowire._

object App extends JSApp {
  /**
    * Main entry point
    */
  def main() = {
    ReactDOM.render(TodoControl.component(), document.getElementById("root"))
  }
}

object AppService {
  val basePath = dom.document.location.origin.toOption.getOrElse("")
  val client = new Client(basePath)

  def addTodo(text: String): Future[Unit] = client[Api].addTodo(text).call()
  def allTodos(): Future[Seq[TodoItem]] = client[Api].allTodos().call()
}

class Client(val basePath:String) extends autowire.Client[Js.Value, Reader, Writer]{
  override def doCall(req: Request): Future[Js.Value] = {
    dom.ext.Ajax.post(
      url = s"$basePath/api/" + req.path.mkString("/"),
      data = upickle.json.write(Js.Obj(req.args.toSeq:_*))
    ).map(r => upickle.json.read(r.responseText))
  }

  def read[Result: Reader](p: Js.Value) = readJs[Result](p)
  def write[Result: Writer](r: Result) = writeJs(r)
}
