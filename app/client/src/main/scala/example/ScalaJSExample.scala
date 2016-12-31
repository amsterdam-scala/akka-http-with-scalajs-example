package example

import org.scalajs.dom
import shared.SharedMessages

object ScalaJSExample extends scala.scalajs.js.JSApp {
  def main(): Unit = dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
}