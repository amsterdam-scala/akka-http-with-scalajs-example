package example

import com.karasiq.bootstrap.BootstrapImplicits._
import com.karasiq.bootstrap.grid.GridSystem
import com.karasiq.bootstrap.icons.FontAwesome
import com.karasiq.bootstrap.navbar.{NavigationBarStyle, NavigationTab}
import org.scalajs.dom
import rx._

import scala.language.{existentials, postfixOps}
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._
import scalatags.JsDom.short.*

@JSExport
object BootstrapTestApp extends JSApp {
  private implicit val context = implicitly[Ctx.Owner] // Stops working if moved to main(), macro magic

  def main(): Unit = {

    def navigationBar = {
      val imgStyle = Seq(*.height := 42.px, *.width := 42.px, *.marginTop := 4.px)

      NavigationBar2()
        .withBrand(a(href := "http://getbootstrap.com/components/#navbar")("AkkaHTTP", br, "RESTless"))
        .withElementsLeft(a(href := "http://www.scala-lang.org/", target := "_blank")
        (img(imgStyle)(src := "/img/scala-5.svg", title := "Scala language")),

          a(href := "http://www.scala-js.org/", target := "_blank")
          (img(imgStyle)(src := "/img/scala-js-logo.svg", title := "Scala.js")),

          a(href := "http://doc.akka.io/docs/akka-http/10.0.1/scala/http/introduction.html", target := "_blank")
          (img(imgStyle)(src := "/img/akka512.png", title := "The Streaming-first HTTP server/module of Akka.")),

          //img(src := "/img/logo-React.svg", title := "Facebook's React"),
          img(imgStyle)(src := "/img/api-graphic.jpg", title := "RESTless web service"),

          img(imgStyle)(src := "/img/logo-Bootstrap.svg", title := "Twitter Bootstrap"))
        .withTabs(
          //          NavigationTab(tabTitle, "table", "table".fontAwesome(FontAwesome.fixedWidth), new TestTable, tableVisible.reactiveShow),
          NavigationTab("Carousel", "carousel", "picture", new TodoList()),
          NavigationTab("ToDo list", "todo", "fort-awesome".fontAwesome(FontAwesome.fixedWidth), new TodoList())
        )
        .withContentContainer(content ⇒ GridSystem.container(id := "main-container", GridSystem.mkRow(content)))
        .withStyles(NavigationBarStyle.inverse, NavigationBarStyle.fixedTop)
        .withElementsRight(
          form(action := "https://github.com/search", cls := "navbar-form navbar-right", method := "GET", target := "_blank")
          (input(`type` := "hidden", name := "nwo", value := "amsterdam-scala/akka-http-with-scalajs-example")
            , input(name := "search_target", `type` := "hidden", value := "repository")
            , input(name := "ref", `type` := "hidden", value := "cmdform")
            , input(cls := "form-control", name := "q", placeholder := "Search GitHub…", `type` := "text")),
          a(*.paddingRight := 0, href := "http://www.scala-lang.org/", target := "_blank")("Scala")
          , a(*.paddingRight := 0, href := "http://www.scala-js.org/", target := "_blank")("Scala.js")
          , a(*.paddingRight := 0, href := "http://doc.akka.io/docs/akka-http/10.0.1/scala/http/introduction.html", target := "_blank")
          ("Akka HTTP")
        )
        .build()
    }

    // Render page
    navigationBar.applyTo(dom.document.body)

    // Reactive navbar test
    //      navigationBar.addTabs(NavigationTab("Buttons", "buttons", "log-in", new TestPanel("Serious business panel", PanelStyle.warning)))
    //navigationBar.selectTab(0)
  }


}
