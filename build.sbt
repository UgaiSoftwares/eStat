name := """eStat"""

version := "1.0-SNAPSHOT"

resolvers += Resolver.url("Edulify Repository", url("http://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)

// resolvers += "micronautics/play on bintray" at "http://dl.bintray.com/micronautics/play"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.apache.jena" % "apache-jena-libs" % "2.13.0",
  "com.edulify" %% "play-hikaricp" % "2.0.3",
  "org.webjars" %% "webjars-play" % "2.3.0-3",
  "org.webjars" % "bootstrap" % "3.3.2-2",
  "org.webjars" % "d3js" % "3.5.3",
  "org.webjars" % "c3" % "0.4.9",
  ws
)

scalariformSettings

// ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;.*AuthService.*;models\\.data\\..*"

