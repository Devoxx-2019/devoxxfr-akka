package com.github.nsphung.devoxxfr.akka.services

import play.api.libs.ws.StandaloneWSClient

import scala.concurrent.Future

object ScalaClient {

  val MOCK_URL = "http://localhost:8080/castlemock/mock/rest/project/Ip2QQe/application/qEYIZg/"

  import play.api.libs.ws.DefaultBodyReadables._

  import scala.concurrent.ExecutionContext.Implicits._

  def call(wsClient: StandaloneWSClient): Future[Int] = {
    wsClient.url(MOCK_URL).get().map { response ⇒
      val statusText: String = response.statusText
      val body = response.body[String]
      println(s"Got a response $statusText with body $body")
      1
    }
  }
}
