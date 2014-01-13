import sbt._
import sbt.Keys._

object NyflyverBuild extends Build {

  lazy val nyflyver = Project(
    id = "nyflyver",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "NyFlyver",
      organization := "zzz.akka",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.2",
      scalacOptions ++= Seq("-feature", "-deprecation"),
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies ++= Seq(
          "org.scalatest" % "scalatest_2.10" % "2.0.M5b",
          "com.typesafe.akka" %% "akka-testkit" % "2.1.0",
          "com.typesafe.akka" %% "akka-actor" % "2.1.2"
        )

    )
  )
}
