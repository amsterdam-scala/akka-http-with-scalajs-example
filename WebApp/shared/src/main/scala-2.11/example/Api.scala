package example

import rx.Var

case class TodoItem0(message: String, timestamp: String) {
  def view() = s"$message [$timestamp]"
}

trait Api {
  def allTodo(): Iterable[Task]

  def createTodo(taskWithoutId: String): Iterable[Task]

  def update(task: Task): Task

  def delete(ids: String): Iterable[Task]

  def clearCompletedTasks(): Iterable[Task]
}

case class Task(txt: String, done: Boolean)

case class Task0(txt: Var[String], done: Var[Boolean])
