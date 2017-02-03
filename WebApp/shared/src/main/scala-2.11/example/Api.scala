package example

trait Api {

  def allTodo0(): Iterable[Task0]

  def createTodo(taskWithoutId: Task0): Task0

  def updateTodo(oldTask: Task0, newTask: Task0): Task0

  def deleteTodo(itemToDelete: Task0): Task0

  def clearCompletedTasks(): Iterable[Task0]

}

case class Task0(id: Option[Long] = None, txt: String, done: Boolean)