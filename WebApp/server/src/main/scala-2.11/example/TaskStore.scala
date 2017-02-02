package example


object TaskStore {
  def initAll = Seq(
    Task0(None, "Todo MVC Task A", false),
    Task0(None, "Todo MVC Task B", true),
    Task0(None, "Todo MVC Task C", false)
  )

var selectAll = initAll

/*
  def all: Future[Seq[TodoItem0]]

  def create(taskWithoutId: TodoItem0): Future[TodoItem0]

  def update(task: TodoItem0): Future[Boolean]

  def delete(ids: Long*): Future[Boolean]

  def clearCompletedTasks: Future[Int]
*/
}
