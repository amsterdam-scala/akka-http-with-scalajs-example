package example

import example.Model.TodoItem
import japgolly.scalajs.react.ReactComponentC.{ConstProps, ReqProps}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.ReactTagOf
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.html.Div

import scala.concurrent.ExecutionContext.Implicits.global

object TodoControl {
  val TodoList: ReqProps[Seq[TodoItem], Unit, Unit, TopNode] =
    ReactComponentB[Seq[TodoItem]]("TodoList").render_P { props =>
      def createItem(item: TodoItem) = <.li(item.view())
      <.ul(props map createItem)
    }.build
  val component: ConstProps[Unit, State, Backend, TopNode] = ReactComponentB[Unit]("TodoControl")
    .initialState(State(Nil, ""))
    .renderBackend[Backend]
    .componentDidMount(_.backend.load())
    .build

  case class State(items: Seq[TodoItem], text: String)

  class Backend($: BackendScope[Unit, State]) {
    def render(state: State): ReactTagOf[Div] =
      <.div(
        <.h3("TODO"),
        TodoList(state.items),
        <.form(^.onSubmit ==> handleSubmit,
          <.input(^.onChange ==> onChange, ^.value := state.text),
          <.button("Add #", state.items.length + 1)
        )
      )

    def onChange(e: ReactEventI): Callback = {
      val newValue = e.target.value
      $.modState(_.copy(text = newValue))
    }

    def handleSubmit(e: ReactEventI): Callback = Callback.future {
      e.preventDefault()
      val v = $.state.runNow().text
      AppService.addTodo(v).map(_ => $.modState(s => s.copy(text = ""), load()))
    }

    def load(): Callback = Callback.future {
      AppService.allTodos().map(data => $.modState(s => s.copy(items = data)))
    }
  }
}
