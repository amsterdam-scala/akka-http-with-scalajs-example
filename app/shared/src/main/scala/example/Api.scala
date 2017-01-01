package example

import example.Model.TodoItem

trait Api {
  def addTodo(text: String): Unit

  def allTodos(): Seq[TodoItem]
}
