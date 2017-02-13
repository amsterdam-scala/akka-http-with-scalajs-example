package example

import scalatags.Text.all._

object TestPageAssets {
  def index: String = {
    def comment(comment: String) = raw("<!-- " + scalatags.Escaping.escape(comment, new StringBuilder) + " -->")

    def hiddenConditionalComment(condition: String, xs: scalatags.Text.Modifier*) =
      Seq(raw(s"<!--[if $condition]>")) ++ xs.toSeq :+ raw("<![endif]-->")

    def styleText: String = {
      """
        |#main-container {
        |    padding: 20px;
        |}
        |
        |@media (min-width: 768px) {
        |    #main-container {
        |        max-width: 80%;
        |    }
        |}
        |
        |td.buttons {
        |    text-align: center;
        |}
        |
        |.panel-primary .panel-head-buttons .glyphicon {
        |    color: white;
        |}
        |
        |.glyphicon {
        |    margin-left: 2px;
        |    margin-right: 2px;
        |}
        |
        |.panel-title .glyphicon {
        |    margin-right: 10px;
        |}
        |
        |#main-container {
        |    margin-top: 50px;
        |}
      """.stripMargin.split('\n').map(_.trim.filter(_ >= ' ')).mkString
    }

    def browserUpgrade =
      p(cls := "browserupgrade", "You are using an ", strong("outdated"), " browser. Please ",
        a(href := "http://browsehappy.com/", "upgrade your browser"), " to improve your experience.")

    "<!DOCTYPE html>" + html(lang := "",
      head(
        meta(charset := "UTF-8"),
        meta(content := "IE=edge", httpEquiv := "X-UA-Compatible"),
        meta(content := "initial-scale=1, shrink-to-fit=no, width=device-width", name := "viewport"),
        //The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags.

        meta(name := "author", content := "FvdB"),
        meta(name := "description", content := "Akka HTTP RESTless Web App with react, bootstrap as a sbt-web-scalajs project with webjars"),
        meta(name := "generator", content := "Scala.js Scalatags"),
        meta(name := "versionId", content := "2017-01"),

        scalatags.Text.tags2.title("Akka HTTP RESTless web app"),
        link(rel := "apple-touch-icon", href := "/img/apple-touch-icon.png"),
        link(rel := "icon", `type` := "image/x-icon", href := "/img/favicon.ico"),

        //Latest compiled and minified CSS
        link(
          attr("crossOrigin") := "anonymous",
          attr("integrity") := "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u",
          href := "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css",
          media := "screen", rel := "stylesheet"),

        link(attr("crossOrigin") := "anonymous",
          href := "https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css",
          media := "screen", rel := "stylesheet"),
        //Custom styles for this page
        //link(rel := "stylesheet", href := "/css/main.css", media := "screen"),

        scalatags.Text.tags2.style(raw(styleText))
      ),
      body(hiddenConditionalComment("lt IE 9", Seq(browserUpgrade)),
        script(`type` := "text/javascript", src := "/js/main-jsdeps.js"),
        // Include Scala.js compiled code
        script(`type` := "text/javascript", src := "/js/main-fastopt.js"),
        //Run JSApp
        script(`type` := "text/javascript", src := "/js/main-launcher.js")))
  }
}
