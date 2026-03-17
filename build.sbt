import org.tmt.sbt.docs.Settings
import org.tmt.sbt.docs.DocKeys.*
import sbt.Test

lazy val githubRepoUrl = "https://github.com/your-username/your-project"

ThisBuild / scalaVersion := "3.6.4"
ThisBuild / organizationName := "TMT Org"
ThisBuild / docsRepo := githubRepoUrl
ThisBuild / docsParentDir := ""
ThisBuild / gitCurrentRepo := githubRepoUrl

version := "0.1.0"

lazy val openSite =
  Def.setting {
    Command.command("openSite") { state =>
      val uri = s"file://${Project.extract(state).get(siteDirectory)}/${docsParentDir.value}/${version.value}/index.html"
      state.log.info(s"Opening browser at $uri ...")
      java.awt.Desktop.getDesktop.browse(new java.net.URI(uri))
      state
    }
  }



/* ================= Root Project ============== */
lazy val `apsproceduredataservice` = project
  .in(file("."))
  .enablePlugins(GithubPublishPlugin)
  .aggregate(docs)
  .settings(
    ghpagesBranch := "gh-pages", // DO NOT DELETE
    commands += openSite.value,
    Settings.makeSiteMappings(docs),
    inThisBuild (
      List(
        scalaVersion := "3.6.4",
        version := "0.1.0"
      )
    ),
    fork := true,
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      Libs.`esw-http-template-wiring` % "compile->compile;test->test",
      Libs.`embedded-keycloak` % Test,
      Libs.`scalatest` % Test,
      Libs.`pekko-http-testkit` % Test,
      Libs.`mockito` % Test,
      Libs.`junit4-interface` % Test,
      Libs.`testng-6-7` % Test,
      Libs.`pekko-actor-testkit-typed` % Test,
      Libs.`pekko-stream-testkit` % Test
    ),
    Test / fork := true
  )

/* ================= Paradox Docs ============== */
lazy val docs = project
  .enablePlugins(ParadoxMaterialSitePlugin)
