package example

import org.scalajs.dom
import rx._

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.section
import japgolly.scalajs.react.ReactDOM
import org.scalajs.dom.html.{Element, Input}
import org.scalajs.jquery.jQuery

@JSExport
object ScalaJSExample {
  import Framework._

  private val editing = Var[Option[Task0]](None)

  private val tasks = Var(
    Seq(
      Task0(Var("Todo MVC Task A"), Var(true)),
      Task0(Var("Todo MVC Task B"), Var(false)),
      Task0(Var("Todo MVC Task C"), Var(false))
    )
  )

  private val filter = Var("All")

  val filters: Map[String, (Task0) => Boolean] = Map[String, Task0 => Boolean](
    ("All", t => true),
    ("Active", !_.done.now),
    ("Completed", _.done.now)
  )

  val done = Rx{tasks().count(_.done())}

  val notDone = Rx{tasks().length - done()}

  val inputBox: Input = input(
    id:="new-todo",
    placeholder:="What needs to be done?",
    autofocus:=true
  ).render

  @JSExport
  def main(): Unit = {
    import Ctx.Owner.Unsafe._
    // dom.document.body.innerHTML = ""
    val component: Element = section(id := "todoapp")(
      header(id := "header")(
        h1("todos"),
        form(
          inputBox,
          onsubmit := { () =>
            AppService.addTodo(inputBox.value)
            tasks() = Task0(Var(inputBox.value), Var(false)) +: tasks.now
            inputBox.value = ""
            false
          }
        )
      ),
      section(id := "main")(
        input(
          id := "toggle-all",
          `type` := "checkbox",
          cursor := "pointer",
          onclick := { () =>
            val target = tasks.now.exists(_.done.now == false)
            Var.set(tasks.now.map(_.done -> target): _*)
          }
        ),
        label(`for` := "toggle-all", "Mark all as complete"),
        Rx {
          ul(id := "todo-list")(
            for (task <- tasks() if filters(filter())(task)) yield {
              val inputRef = input(`class` := "edit", value := task.txt()).render

              li(
                `class` := Rx {
                  if (task.done()) "completed"
                  else if (editing().contains(task)) "editing"
                  else ""
                },
                div(`class` := "view")(
                  ondblclick := { () =>
                    editing() = Some(task)
                  },
                  input(
                    `class` := "toggle",
                    `type` := "checkbox",
                    cursor := "pointer",
                    onchange := { () =>
                      task.done() = !task.done.now
                    },
                    if (task.done()) checked := true
                  ),
                  label(task.txt()),
                  button(
                    `class` := "destroy",
                    cursor := "pointer",
                    onclick := { () => tasks() = tasks().filter(_ != task) }
                  )
                ),
                form(
                  onsubmit := { () =>
                    task.txt() = inputRef.value
                    editing() = None
                    false
                  },
                  inputRef
                )
              )
            }
          )
        },
        footer(id := "footer")(
          span(id := "todo-count")(strong(notDone), " item left"),

          ul(id := "filters")(
            for ((name, pred) <- filters.toSeq) yield {
              li(a(
                `class` := Rx {
                  if (name == filter()) "selected"
                  else ""
                },
                name,
                href := "#",
                onclick := { () => filter() = name }
              ))
            }
          ),
          button(
            id := "clear-completed",
            onclick := { () => tasks() = tasks.now.filter(!_.done.now) },
            "Clear completed (", done, ")"
          )
        )
      ),
      footer(id := "info")(
        p("Double-click to edit a todo"),
        p(a(href := "https://github.com/lihaoyi/workbench-example-app/blob/todomvc/src/main/scala/example/ScalaJSExample.scala")("Source Code")),
        p("Created by ", a(href := "http://github.com/lihaoyi")("Li Haoyi"))
      )
    ).render

    jQuery("body").append(component)

  }
}
