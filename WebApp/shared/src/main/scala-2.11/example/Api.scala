package example

import rx.Var

case class TodoItem0(message: String, timestamp: String) {
  def view() = s"$message [$timestamp]"
}

trait Api {
  def addTodo(text: String): Unit

  def allTodos(): Seq[TodoItem0]
}

case class Task(txt: Var[String], done: Var[Boolean])
