import Dependencies._

lazy val root = (project in file(".")).settings(
  name := "twitter-akka-streams",
  organization := "com.github.aholg",
  version := "0.1",
  scalaVersion := "2.12.3",
  libraryDependencies ++= Seq(akkaStream, akkaHttp, akkaHttpTestKit)
)
