package example

case class TodoItem0(message: String, timestamp: String) {
  def view() = s"$message [$timestamp]"
}

trait Api {

  def allTodo0(): Iterable[Task0]

  def createTodo(taskWithoutId: Task0): Task0

//  def update(task: Task): Task

//  def delete(ids: String): Iterable[Task]

  def clearCompletedTasks(): Iterable[Task0]

}

case class Task0(id: Option[Long] = None, txt: String, done: Boolean)