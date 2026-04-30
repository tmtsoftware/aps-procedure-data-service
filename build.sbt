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

// ── jOOQ code generation task ──────────────────────────────────────────────
lazy val generateJooq = taskKey[Unit]("Run jOOQ code generator")

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
      Libs.`pekko-stream-testkit` % Test,
      Libs.`csw-database`,
      Libs.`jooq`,
      Libs.`postgresql`
    ),
    Test / fork := true,


    // ── jOOQ codegen task ────────────────────────────────────────────────────
    generateJooq := {
      val log = streams.value.log
      log.info("Running jOOQ code generator...")

      val jdbc = new org.jooq.meta.jaxb.Jdbc()
        .withDriver("org.postgresql.Driver")
        .withUrl("jdbc:postgresql://localhost:5432/peas")
        .withUser("postgres")
        .withPassword("postgres")

      val database = new org.jooq.meta.jaxb.Database()
        .withName("org.jooq.meta.postgres.PostgresDatabase")
        .withIncludes("Procedure|ProcedureIteration|ProcedureResult")
        .withInputSchema("public")

      val target = new org.jooq.meta.jaxb.Target()
        .withPackageName("org.tmt.apsproceduredataservice.db.generated")
        .withDirectory("src/main/java")

      val generate = new org.jooq.meta.jaxb.Generate()
        .withPojos(true)
        .withDaos(false)

      val configuration = new org.jooq.meta.jaxb.Configuration()
        .withJdbc(jdbc)
        .withGenerator(
          new org.jooq.meta.jaxb.Generator()
            .withDatabase(database)
            .withGenerate(generate)
            .withTarget(target)
        )

      new org.jooq.codegen.GenerationTool().run(configuration)
      log.info("jOOQ code generation complete.")
    }
  )


/* ================= Paradox Docs ============== */
lazy val docs = project
  .enablePlugins(ParadoxMaterialSitePlugin)
