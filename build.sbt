import AssemblyKeys._ // put this at the top of the file

assemblySettings

name := "sifter"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.2" % "test"

// resolvers ++= Seq(
//       "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
//       "releases"  at "http://oss.sonatype.org/content/repositories/releases",
//       "Concurrent Maven Repo" at "http://conjars.org/repo",
//       "Codahale" at "http://repo.typesafe.com/typesafe/releases"
//     )
