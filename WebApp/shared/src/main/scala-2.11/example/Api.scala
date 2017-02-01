package example

import rx.Var

case class TodoItem0(message: String, timestamp: String) {
  def view() = s"$message [$timestamp]"
}

trait Api {
  def allTodo(): Iterable[Task]

  def allTodo0(): Iterable[Task0]

  def createTodo(taskWithoutId: String): Iterable[Task]

  def update(task: Task): Task

  def delete(ids: String): Iterable[Task]

  def clearCompletedTasks(): Iterable[Task]

}

object Api {  def tasks00 = Seq(
  Task0(None, "Todo MVC Task A", false),
  Task0(None, "Todo MVC Task B", true),
  Task0(None, "Todo MVC Task C", false)
)
}

case class Task(txt: String, done: Boolean)

case class Task0(id: Option[Long] = None, txt: String, done: Boolean)

