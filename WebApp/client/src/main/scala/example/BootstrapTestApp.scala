package example

import com.karasiq.bootstrap.BootstrapImplicits._
import com.karasiq.bootstrap.grid.GridSystem
import com.karasiq.bootstrap.icons.FontAwesome
import com.karasiq.bootstrap.navbar.{NavigationBar, NavigationBarStyle, NavigationTab}
import org.scalajs.dom
import rx._

import scala.language.postfixOps
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all.{img, _}

@JSExport
object BootstrapTestApp extends JSApp {
  private implicit val context = implicitly[Ctx.Owner] // Stops working if moved to main(), macro magic

  def main(): Unit = {
    //jQuery(() ⇒ {

    def navigationBar = NavigationBar()
      .withBrand(div(a("Akka HTTP RESTless web app", href := "http://getbootstrap.com/components/#navbar"),
        a(href := "http://www.scala-lang.org/", target := "_blank")
        (img(src := "/img/scala-5.svg", height := 42, width:=42, title := "Scala language")),

        a(href := "http://www.scala-js.org/", target := "_blank")
        (img(src := "/img/scala-js-logo.svg", height := 42, title := "Scala.js")),

        a(href := "http://doc.akka.io/docs/akka-http/10.0.1/scala/http/introduction.html", target := "_blank")
        (img(src := "/img/akka512.png", height := 42, width := 42, title := "The Streaming-first HTTP server/module of Akka.")),

        //img(src := "/img/logo-React.svg", height := "42", attr("height") := "42", title := "Facebook's React"),
        img(src := "/img/api-graphic.jpg", height := 42, title := "RESTless web service"),

        img(src := "/img/logo-Bootstrap.svg", height := 42, title := "Twitter Bootstrap"))

      )
      .withTabs(
        //          NavigationTab(tabTitle, "table", "table".fontAwesome(FontAwesome.fixedWidth), new TestTable, tableVisible.reactiveShow),
        NavigationTab("Carousel", "carousel", "picture", new TodoList()),
        NavigationTab("ToDo list", "todo", "fort-awesome".fontAwesome(FontAwesome.fixedWidth), new TodoList())
      )
      .withContentContainer(content ⇒ GridSystem.container(id := "main-container", GridSystem.mkRow(content)))
      .withStyles(NavigationBarStyle.inverse, NavigationBarStyle.fixedTop, NavigationBarStyle.default)
      .build()

    // Render page
    navigationBar.applyTo(dom.document.body)

    // Reactive navbar test
    //      navigationBar.addTabs(NavigationTab("Buttons", "buttons", "log-in", new TestPanel("Serious business panel", PanelStyle.warning)))
    //navigationBar.selectTab(0)
    /*})*/
  }
}
