package example

import japgolly.scalajs.react.vdom.ReactTagOf
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactEventI}

import scala.concurrent.ExecutionContext.Implicits.global

object TodoControl {
  val TodoList =
    ReactComponentB[Seq[TodoItem0]]("TodoList").render_P { props =>
      def createItem(item: TodoItem0) = <.li(item.view())

      <.ul(props map createItem)
    }.build
  val component = ReactComponentB[Unit]("TodoControl")
    .initialState(State(Nil, ""))
    .renderBackend[Backend]
    .componentDidMount(_.backend.load())
    .build

  case class State(items: Seq[TodoItem0], text: String)

  class Backend($: BackendScope[Unit, State]) {
    def render(state: State): ReactTagOf[org.scalajs.dom.html.Div] =
      <.div(
        <.h3("TODO"),
        TodoList(state.items),
        <.form(^.onSubmit ==> handleSubmit,
          <.input(^.onChange ==> onChange, ^.value := state.text),
          <.button("Add #", state.items.length + 1)
        )
      )

    def onChange(e: ReactEventI) = {
      val newValue = e.target.value
      $.modState(_.copy(text = newValue))
    }

    def handleSubmit(e: ReactEventI) = Callback.future {
      e.preventDefault()
      val v = $.state.runNow().text
      AppService.addTodo(v).map(_ => $.modState(s => s.copy(text = ""), load()))
    }

    def load(): Callback = Callback.future {
      AppService.allTodos().map(data => $.modState(s => s.copy(items = data)))
    }
  }

}