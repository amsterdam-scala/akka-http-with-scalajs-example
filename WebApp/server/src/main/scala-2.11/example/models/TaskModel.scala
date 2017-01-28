package example
package models

import scala.concurrent.Future


trait TaskStore {


  def all: Future[Seq[TodoItem0]]

  def create(taskWithoutId: TodoItem0): Future[TodoItem0]

  def update(task: TodoItem0): Future[Boolean]

  def delete(ids: Long*): Future[Boolean]

  def clearCompletedTasks: Future[Int]
}
