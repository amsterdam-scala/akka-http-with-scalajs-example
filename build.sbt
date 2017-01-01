lazy val commonSettings = Seq(
                  name := "Empty webapp Akka HTTP, react with sbtWebScalajs",
               version := "0.1-SNAPSHOT",
           description := "Simple example application showing how you can integrate an Akka HTTP project with a Scala.js project",
          organization := "nl.amsscala",
      organizationName := "Amsterdam.scala Meetup Group",
  organizationHomepage := Some(url("http://www.meetup.com/amsterdam-scala/")),
              homepage := Some(url("http://github.com/amsterdam-scala/akka-http-with-scalajs-example")),
             startYear := Some(2016),
  licenses += "EUPL-1.1" -> url("http://joinup.ec.europa.eu/community/eupl/og_page/european-union-public-licence-eupl-v11"),

          scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    // "com.lihaoyi" %%% "scalatags" % "0.6.2",
    // "com.lihaoyi" %%% "autowire" % autowireV,
    // "com.lihaoyi" %%% "upickle" % upickleV
  ))

lazy val akkaHttpV   = "10.0.1"
lazy val autowireV   = "0.2.6"
lazy val reactV      = "15.4.1"
lazy val scalaDomV   = "0.9.1"
lazy val scaJSreactV = "0.11.3"
lazy val scalaV      = "2.12.1"
lazy val upickleV    = "0.4.4"

scalaVersion := scalaV

lazy val client = (project in file("app/client")).settings(
  commonSettings,
  jsDependencies ++= Seq(
    "org.webjars.bower" % "react" % reactV / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
    "org.webjars.bower" % "react" % reactV / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM",
    "org.webjars.bower" % "react" % reactV / "react-dom-server.js" minified "react-dom-server.min.js" dependsOn "react-dom.js" commonJSName "ReactDOMServer"
  ),
  libraryDependencies ++= Seq(
    "com.github.japgolly.scalajs-react" %%% "core" % scaJSreactV,
    "com.github.japgolly.scalajs-react" %%% "ext-monocle" % scaJSreactV,
    "com.github.japgolly.scalajs-react" %%% "ext-scalaz72" % scaJSreactV,
    "com.github.japgolly.scalajs-react" %%% "extra" % scaJSreactV,
    "org.scala-js" %%% "scalajs-dom" % scalaDomV,
    "com.lihaoyi" %%% "autowire" % autowireV,
    "com.lihaoyi" %%% "upickle" % upickleV
  ),
  // KEEP THIS normalizedName CONSTANTLY THE SAME, otherwise the outputted JS filename will be changed.
  normalizedName := "main",
  persistLauncher := true,
  persistLauncher in Test := false,
  scalaVersion := scalaV
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).dependsOn(sharedJs)

lazy val server = (project in file("app/server")).settings(
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for Twirl templates are present
  //EclipseKeys.preTasks := Seq(compile in Compile)
  libraryDependencies += "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  managedClasspath in Runtime += (packageBin in Assets).value,
  pipelineStages in Assets := Seq(scalaJSPipeline),
  scalaJSProjects := Seq(client),
  scalaVersion := scalaV,
  WebKeys.packagePrefix in Assets := "public/"
  ).enablePlugins(SbtWeb /*, SbtTwirl , JavaAppPackaging*/).dependsOn(sharedJvm)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("app/shared")).
  settings(commonSettings).jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) // compose (onLoad in Global).value
