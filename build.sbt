lazy val common = Seq(
  organization := "kipalog",
  version := "0.0.1",
  scalaVersion := "2.10.5",
  homepage := Some(new java.net.URL("http://kipalog.com"))
)

val knockOffVersion = "0.8.3"
lazy val knockOffDep = Def.setting { Seq(
  "com.tristanhunt" %% "knockoff" % knockOffVersion
)}
val unfilteredVersion = "0.8.3"
lazy val unfilterDeps = Def.setting { Seq(
  "net.databinder" %% "unfiltered-filter" % unfilteredVersion,
  "net.databinder" %% "unfiltered-jetty" % unfilteredVersion
)}

lazy val scaresume: Project = (project in file("."))
  .settings(common: _*)
  .settings(
    name := "scalresume",
    description := "generate your beautiful resume from markdown",
    libraryDependencies ++= knockOffDep.value ++ unfilterDeps.value,
    fork in run := true
  )

