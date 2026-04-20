resolvers += "jitpack" at "https://jitpack.io"
libraryDependencies += "com.github.tmtsoftware" % "sbt-docs"     % "0.7.1"

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.4")

// jOOQ codegen — must be here so GenerationTool is on the sbt classpath
libraryDependencies += "org.jooq"       % "jooq-codegen" % "3.19.6"
libraryDependencies += "org.jooq"       % "jooq-meta"    % "3.19.6"
libraryDependencies += "org.jooq"       % "jooq"         % "3.19.6"
libraryDependencies += "org.postgresql" % "postgresql"   % "42.7.3"