package example

object TaskStore {
  val selectAll = scala.collection.mutable.ListBuffer[Task0]()
  var sysChangeNr = 0L

  def create(taskWithoutId: Task0): Task0 = {
    val ret = taskWithoutId.copy(id = Some(atomicSCN()))
    selectAll += ret
    ret
  }

  @inline
  def atomicSCN(): Long = {
    this.synchronized {
      val ret = sysChangeNr
      sysChangeNr += 1L
      ret
    }
  }

  def delete(task: Task0): Task0 = {
    atomicSCN()
    selectAll -= task
    task
  }

  /*  def update(task: TodoItem0): Future[Boolean]


    def clearCompletedTasks: Future[Int]
  */

  create(Task0(None, "Upgrade Scala JS", true))
  create(Task0(None, "Make it Rx", false))
  create(Task0(None, "Make this example useful", false))
}