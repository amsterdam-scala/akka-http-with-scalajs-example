val scalaV = "2.12.1"
scalaVersion := scalaV

lazy val client = (project in file("app/client")).settings(
  libraryDependencies ++= Seq("org.scala-js" %%% "scalajs-dom" % "0.9.1"),
  persistLauncher := true,
  persistLauncher in Test := false,
  scalaVersion := scalaV
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).dependsOn(sharedJs)

lazy val server = (project in file("app/server")).settings(
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for Twirl templates are present
  //EclipseKeys.preTasks := Seq(compile in Compile)
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http"       % "10.0.1"/*,
    "com.vmunier"       %% "scalajs-scripts" % "1.1.0"*/),
  managedClasspath in Runtime += (packageBin in Assets).value,
  pipelineStages in Assets := Seq(scalaJSPipeline),
  scalaJSProjects := Seq(client),
  scalaVersion := scalaV,
  WebKeys.packagePrefix in Assets := "public/"
  ).enablePlugins(SbtWeb /*, SbtTwirl , JavaAppPackaging*/).dependsOn(sharedJvm)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("app/shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) // compose (onLoad in Global).value
