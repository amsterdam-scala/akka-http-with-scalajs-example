package example

import com.karasiq.bootstrap.BootstrapAttrs.{`data-target`, `data-toggle`}
import com.karasiq.bootstrap.BootstrapImplicits._
import com.karasiq.bootstrap.grid.GridSystem
import com.karasiq.bootstrap.icons.IconModifier.NoIcon
import com.karasiq.bootstrap.navbar.{NavigationBarStyle, NavigationTab}
import com.karasiq.bootstrap.{Bootstrap, BootstrapComponent}
import org.scalajs.jquery.jQuery
import rx._

import scalatags.JsDom.all._

/**
  * Simple bootstrap navigation bar
  */
final class NavigationBar2(barId: String,
                           brand: Modifier,
                           styles: Seq[NavigationBarStyle],
                           elemLeft: Seq[Modifier],
                           elemRight : Seq[Modifier],
                           container: Modifier ⇒ Modifier,
                           contentContainer: Modifier ⇒ Modifier)
                          (implicit ctx: Ctx.Owner) extends BootstrapComponent {
  val navigationTabs: Var[Seq[NavigationTab]] = Var(Nil)
  private val nav = tag("nav")
  private val tabContainer = Rx {
    def renderTab(active: Boolean, tab: NavigationTab): Tag = {
      val idLink = s"$barId-${tab.id}-tab"
      li(tab.modifiers,
        "active".classIf(active),
        a(href := "#", aria.controls := idLink, role := "tab", `data-toggle` := "tab", `data-target` := s"#$idLink")(
          if (tab.icon == NoIcon) () else Seq[Modifier](tab.icon, raw("&nbsp;")),
          tab.name
        )
      )
    }

    val tabs = navigationTabs()
    ul(`class` := "nav navbar-nav")(
      // renderTab(active = true, tabs.head),
      for (tab <- tabs) yield renderTab(active = tab == tabs.head, tab)
    )
  }

  def navbarRight (links : Modifier*) =
    {ul(cls := "nav navbar-nav navbar-right")(
      for (link <- links) yield li(link)
    )

    }

  private val tabContentContainer = Rx {
    def renderContent(active: Boolean, tab: NavigationTab): Tag = {
      div(id := s"$barId-${tab.id}-tab", role := "tabpanel", `class` := (if (active) "tab-pane active fade in" else "tab-pane fade"))(
        tab.content
      )
    }

    val tabs = navigationTabs()
    div(id := s"$barId-tabcontent", `class` := "tab-content")(
      renderContent(active = true, tabs.head),
      for (tab <- tabs.tail) yield renderContent(active = false, tab)
    )
  }

  /**
    * Selects tab by index
    *
    * @param i Tab index, starting from `0`
    */
  def selectTab(i: Int): Unit = {
    val tabs = navigationTabs.now
    require(i >= 0 && tabs.length > i, s"Invalid tab index: $i")
    this.selectTab(tabs(i).id)
  }

  /**
    * Selects tab by ID
    *
    * @param id Tab ID
    */
  def selectTab(id: String): Unit = {
    jQuery(s"a[data-target='#$barId-$id-tab']").tab("show")
  }

  /**
    * Appends provided tabs to tab list
    *
    * @param tabs Navbar tabs
    */
  def addTabs(tabs: NavigationTab*): Unit = {
    navigationTabs.update(navigationTabs.now ++ tabs)
  }

  /**
    * Updates tab list
    *
    * @param tabs Navbar tabs
    */
  def setTabs(tabs: NavigationTab*): Unit = {
    navigationTabs.update(tabs)
  }

  def render(md: Modifier*) = {
    Seq(navbar, contentContainer(content))
  }

  def navbar: Tag = {
    nav("navbar".addClass, styles)(
      container(Seq(
        // Header
        div(`class` := "navbar-header")(
          elemLeft,
          button(`type` := "button", `data-toggle` := "collapse", `data-target` := s"#$barId", `class` := "navbar-toggle collapsed")(
            span(`class` := "sr-only", "Toggle navigation"),
            span(`class` := "icon-bar"),
            span(`class` := "icon-bar"),
            span(`class` := "icon-bar")
          ),
          a(href := "#", `class` := "navbar-brand", brand)
        ),
        div(id := barId, `class` := "navbar-collapse collapse")(tabContainer, navbarRight(elemRight))
      ))
    )
  }

  def content: Modifier = {
    tabContentContainer
  }
}

case class NavigationBarBuilder2(tabs: Seq[NavigationTab],
                                 barId: String, brand: Modifier,
                                 styles: Seq[NavigationBarStyle],
                                 elemLeft: Seq[Modifier],
                                 elemRight: Seq[Modifier],
                                 container: Modifier ⇒ Modifier,
                                 contentContainer: Modifier ⇒ Modifier) {
  def build()(implicit ctx: Ctx.Owner) = {
    val bar = new NavigationBar2(barId, brand, styles, elemLeft, elemRight, container, contentContainer)
    bar.addTabs(tabs: _*)
    bar
  }

  def withBrand(brand: Modifier*): NavigationBarBuilder2 = copy(brand = brand)

  def withContainer(container: Modifier ⇒ Modifier) = copy(container = container)

  def withContentContainer(contentContainer: Modifier ⇒ Modifier) = copy(contentContainer = contentContainer)

  def withElementsLeft(elements: Modifier*) = copy(elemLeft = elements)
  def withElementsRight(elements: Modifier*) = copy(elemRight = elements)

  def withId(id: String) = copy(barId = id)

  def withStyles(styles: NavigationBarStyle*) = copy(styles = styles)

  def withTabs(tabs: NavigationTab*) = copy(tabs = tabs)
}

object NavigationBar2 {
  def apply(tabs: Seq[NavigationTab] = Nil,
            barId: String = Bootstrap.newId,
            brand: Modifier = "Navigation",
            styles: Seq[NavigationBarStyle] = Seq(NavigationBarStyle.default, NavigationBarStyle.fixedTop),
            elemLeft: Seq[Modifier] = Nil,
            elemRight: Seq[Modifier] = Nil,
            container: Modifier ⇒ Modifier = md ⇒ GridSystem.container(md),
            contentContainer: Modifier ⇒ Modifier = md ⇒ GridSystem.container(GridSystem.mkRow(md)))
           (implicit ctx: Ctx.Owner) = NavigationBarBuilder2(tabs, barId, brand, styles, elemLeft, elemRight, container, contentContainer)
}
