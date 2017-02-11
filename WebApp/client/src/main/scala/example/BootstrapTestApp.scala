package example

import com.karasiq.bootstrap.BootstrapImplicits._
import com.karasiq.bootstrap.grid.GridSystem
import com.karasiq.bootstrap.icons.FontAwesome
import com.karasiq.bootstrap.navbar.{NavigationBar, NavigationBarStyle, NavigationTab}
import org.scalajs.dom
import org.scalajs.jquery._
import rx._

import scala.language.postfixOps
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._
import scalatags.JsDom.all._

@JSExport
object BootstrapTestApp /*extends JSApp*/ {
  private implicit val context = implicitly[Ctx.Owner] // Stops working if moved to main(), macro magic

  def main0(): Unit = {
    jQuery(() ⇒ {
      // Table tab will appear after 3 seconds
      val tableVisible = Var(false)
      val tabTitle = Var("Wait...")

      val navigationBar = NavigationBar()
        .withBrand("Scala.js Bootstrap Test", href := "http://getbootstrap.com/components/#navbar")
        .withTabs(
          //          NavigationTab(tabTitle, "table", "table".fontAwesome(FontAwesome.fixedWidth), new TestTable, tableVisible.reactiveShow),
          //          NavigationTab("Carousel", "carousel", "picture", new TestCarousel("https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Big_Wood%2C_N2.JPG/1280px-Big_Wood%2C_N2.JPG")),
          NavigationTab("ToDo list", "todo", "fort-awesome".fontAwesome(FontAwesome.fixedWidth), new TodoList)
        )
        .withContentContainer(content ⇒ GridSystem.container(id := "main-container", GridSystem.mkRow(content)))
        .withStyles(NavigationBarStyle.inverse, NavigationBarStyle.fixedTop)
        .build()

      setInterval(3000)({
        tableVisible.update(true)
        setInterval(1000)(tabTitle() = "Table")
      })
      // Render page
      navigationBar.applyTo(dom.document.body)

      // Reactive navbar test
      //      navigationBar.addTabs(NavigationTab("Buttons", "buttons", "log-in", new TestPanel("Serious business panel", PanelStyle.warning)))
      navigationBar.selectTab(1)
    })
  }
}
