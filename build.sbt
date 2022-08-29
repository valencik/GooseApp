// https://typelevel.org/sbt-typelevel/faq.html#what-is-a-base-version-anyway
ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "io.pig"
ThisBuild / organizationName := "io.pig"
ThisBuild / startYear := Some(2022)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("valencik", "Andrew Valencik")
)

// publish to s01.oss.sonatype.org (set to true to publish to oss.sonatype.org instead)
ThisBuild / tlSonatypeUseLegacyHost := false

// publish website from this branch
ThisBuild / tlSitePublishBranch := Some("main")

val Scala213 = "2.13.8"
ThisBuild / crossScalaVersions := Seq(Scala213, "3.1.3")
ThisBuild / scalaVersion := Scala213 // the default Scala

val Http4sVersion = "0.23.15"

lazy val root = tlCrossRootProject.aggregate(core, example)

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(
    name := "gooseapp",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % "2.8.0",
      "org.typelevel" %%% "cats-effect" % "3.3.14",
      "org.typelevel" %%% "cats-effect" % "3.3.14",
      "org.http4s" %%% "http4s-ember-server" % Http4sVersion,
      "org.http4s" %%% "http4s-ember-client" % Http4sVersion,
      "org.http4s" %%% "http4s-circe" % Http4sVersion,
      "org.http4s" %%% "http4s-dsl" % Http4sVersion,
      "org.scalameta" %%% "munit" % "0.7.29" % Test,
      "org.typelevel" %%% "munit-cats-effect-3" % "1.0.7" % Test
    )
  )

lazy val example = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("example"))
  .enablePlugins(NoPublishPlugin)
  .dependsOn(core)

lazy val docs = project.in(file("site")).enablePlugins(TypelevelSitePlugin)
