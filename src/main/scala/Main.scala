import java.nio.file.Paths
import java.time.Instant

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Sink}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object Main extends HttpApp with App {
  implicit val actorSystem = ActorSystem()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val url =
    "https://api.twitter.com/1.1/search/tweets.json?q=%23freebandnames&since_id=24012619984051000&max_id=250126199840518145&result_type=mixed&count=4"

  val eventualStrings: Future[String] = FileIO
    .fromPath(Paths.get("oauth_credentials.json"))
    .map(_.utf8String)
    .runWith(Sink.seq)

  eventualStrings.onComplete{ file=>
    file.toJson
  }

  Http()
    .singleRequest(HttpRequest(HttpMethods.GET, url))
    .map(
      response =>
        response.entity.dataBytes
          .map(_.utf8String)
          .runWith(Sink.foreach(println)))

  override protected def routes: Route = ???
}

object OauthHeaders {

  def buildOauth(consumerKey: String,
                 signature: String,
                 token: String): Seq[HttpHeader] = {
    Seq(
      RawHeader("oauth_consumer_key", consumerKey),
      RawHeader("oauth_nonce", generateNonce),
      RawHeader("oauth_signature", signature),
      RawHeader("oauth_signature_method", "HMAC-SHA1"),
      RawHeader("oauth_timestamp", Instant.now.getEpochSecond.toString),
      RawHeader("oauth_token", token),
      RawHeader("oauth_version", "1.0")
    )
  }

  def generateNonce: String = {
    Random.alphanumeric.take(32).mkString
  }
}
