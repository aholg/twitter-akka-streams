import sbt._

object Dependencies {
  lazy val akkaHttpVersion = "10.0.10"
  lazy val akkaVersion = "2.5.6"

  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.10"
  lazy val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test

  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion

  lazy val sprayJson = "io.spray" %% "spray-json" % "1.3.3"
}
