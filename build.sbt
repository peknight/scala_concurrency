name := "concurrency-examples"

version := "1.0"

scalaVersion := "2.12.7"

resolvers ++= Seq(
    "Sonatype OSS Snapshots" at
    "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype OSS Release" at
    "https://oss.sonatype.org/content/repositories/releases",
    "Typesafe Repository" at
    "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.7"

libraryDependencies += "org.scalaz" %% "scalaz-concurrent" % "7.2.27"

//libraryDependencies += "com.github.scala-blitz" %% "scala-blitz" % "1.2"

// 第六章
libraryDependencies += "com.netflix.rxjava" % "rxjava-scala" % "0.20.7"

// 第六章 P176
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.3"

fork := false

lazy val concurrency = (project in file("."))
  .settings(
    name := "concurrency-examples",
  )
