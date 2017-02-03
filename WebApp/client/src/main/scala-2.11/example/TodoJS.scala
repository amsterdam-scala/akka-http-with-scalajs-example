package example

import org.scalajs.jquery.jQuery
import rx.{Ctx, Rx, Var}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.section

trait Model {
  import Ctx.Owner.Unsafe._

  val tasks = Var(Seq[Task0]())
  val notDone = Rx {tasks().size - done()}
  val done = Rx {tasks().count(_.done)}
  val editing: Var[Option[Task0]] = Var(None)
  val filter = Var("All")
  val filters = Map[String, Task0 => Boolean](("All", t => true), ("Active", !_.done), ("Completed", _.done))

  def clearCompletedTasks = ???

  def update(oldTask : Task0, newTask : Task0)(implicit ctx: Ctx.Data) = {
    tasks() = newTask +: tasks().filter(_ != oldTask)
  }

  def delete(task: Task0)(implicit ctx: Ctx.Data): Unit = tasks() = tasks().filter(_ != task)

  def init(tsks: Iterable[Task0]): Unit = tasks() = tsks.toSeq

  def create(desc: Task0): Unit = tasks() = desc +: tasks.now
}

@JSExport
object TodoJS extends Model {

  import Framework._


  private val inputBox = input(
    id := "new-todo",
    placeholder := "What needs to be done?",
    autofocus := true
  ).render

  @JSExport
  def main(): Unit = {
    import Ctx.Owner.Unsafe._

    def templateFooter = footer(id := "info")(
      p("Double-click to edit a todo"),
      p(a(href := "https://github.com/lihaoyi/workbench-example-app/blob/todomvc/src/main/scala/example/ScalaJSExample.scala")("Source Code")),
      p("Created by ", a(href := "http://github.com/lihaoyi")("Li Haoyi"))
    )

    def templateHeader = header(id := "header")(
      h1("todos"),
      form(
        inputBox,
        onsubmit := { () =>
          App.addTodo(inputBox.value).map(create)
          inputBox.value = ""
          false
        }
      )
    )

    def templateBody(implicit ctx: Ctx.Owner) = {
      def partList = Rx {
        ul(id := "todo-list")(
          for (task <- tasks() if filters(filter())(task)) yield {
            val inputRef = input(`class` := "edit", value := task.txt).render

            li(
              `class` := Rx {if (task.done) "completed" else if (editing().isDefined) "editing" else ""},
              div(`class` := "view")(
                ondblclick := { () =>
                  editing() = Some(task)
                },
                input(
                  `class` := "toggle",
                  `type` := "checkbox",
                  cursor := "pointer",
                  onchange := { () => {
                    /*task = !task.done*/
                  }
                  },
                  if (task.done) checked := true
                ),
                label(task.txt),
                  button(
                    `class` := "destroy",
                    cursor := "pointer",
                    onclick := { () => App.remTodo(task).map(delete)}
                  )
                ),
                form(
                  onsubmit := { () =>
                    {task.copy(txt = inputRef.value)
                    App.updTodo(task, task.copy(txt = inputRef.value)).map(r => update(task, r))
                    editing() = None
                    false}
                  },
                  inputRef
                )
              )
          }
        )
      }

      def partControls = footer(id := "footer")(
        span(id := "todo-count")(strong(notDone), " item left"),

        ul(id := "filters")(
          for (name <- filters.keys.toSeq) yield
            li(a(
              `class` := Rx {
                if (name == filter()) "selected" else ""
              },
              name,
              href := "#",
              onclick := { () => filter() = name }
            ))
        ),
        button(
          id := "clear-completed",
          onclick := { () => clearCompletedTasks /*tasks() = tasks.filter(!_.done)*/},
          "Clear completed (", done, ")"
        )
      )

      section(id := "main")(
        input(
          id := "toggle-all",
          `type` := "checkbox",
          cursor := "pointer",
          onclick := { () => {
            // val target = tasks.now.exists(_.done.now == false)
            // Var.set(tasks.now.map(_.done -> target): _*)
          }
          }
        ),
        label(`for` := "toggle-all", "Mark all as complete"),
        partList,
        partControls
      )
    }


    App.allTodos().map(init)

    jQuery("body").append(section(id := "todoapp",
      templateHeader,
      templateBody,
      templateFooter
    ).render)
  }
}