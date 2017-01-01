package example

object Model {

  case class TodoItem(message: String, timestamp: String) {
    def view() = s"$message [$timestamp]"
  }

}
