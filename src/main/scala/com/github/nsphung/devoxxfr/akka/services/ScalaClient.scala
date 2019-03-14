package com.github.nsphung.devoxxfr.akka.services

import com.codahale.metrics.{Meter, Timer}
import play.api.libs.ws.StandaloneWSClient

import scala.concurrent.Future

object ScalaClient {

  private val requestsMeter: Meter = Metrics.metricRegistry.meter("requests")
  private val requestTimer: Timer = Metrics.metricRegistry.timer("request-timer")

  val MOCK_URL = "http://localhost:8888/castlemock/mock/rest/project/ZQtyo0/application/1xiNEd/toto"
  import play.api.libs.ws.DefaultBodyReadables._

  import scala.concurrent.ExecutionContext.Implicits._

  def call(wsClient: StandaloneWSClient): Future[Int] = {
    val context: Timer.Context = requestTimer.time()
    wsClient.url(MOCK_URL).get().map { response ⇒
      val statusText: String = response.statusText
      val body = response.body[String]
      requestsMeter.mark()
      context.stop()
      //println(s"Got a response $statusText with body $body")
      1
    }
  }
}
