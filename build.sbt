
name := "sifter"

version := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.7"

lazy val root = project.
  in(file(".")).
  aggregate(sifterLib)

lazy val sifterLib = project.
  in(file("sifter-lib"))
