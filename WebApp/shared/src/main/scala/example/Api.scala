package example

case class TodoItem(message: String, timestamp: String) {
  def view() = s"$message [$timestamp]"
}

trait Api {
  def addTodo(text: String): Unit

  def allTodos(): Seq[TodoItem]
}
