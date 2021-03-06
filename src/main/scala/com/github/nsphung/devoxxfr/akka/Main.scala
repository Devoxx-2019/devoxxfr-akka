package com.github.nsphung.devoxxfr.akka

import java.lang.management.ManagementFactory

import akka.NotUsed
import akka.actor.ActorSystem
import akka.japi.function.Function
import akka.stream.Supervision.Directive
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorAttributes, ActorMaterializer, Supervision}
import com.codahale.metrics.Meter
import com.github.nsphung.devoxxfr.akka.models.JsonImplicits._
import com.github.nsphung.devoxxfr.akka.models.{Feature, MyRandom}
import com.github.nsphung.devoxxfr.akka.services.{Metrics, ScalaClient}
import io.circe.syntax._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.control.NonFatal

object Main extends App {
  println("Hello ! This is Reactive Jammed Architecture Demo !")

  // Init Metrics
  Metrics.startMetrics()
  private val inputsMeter: Meter = Metrics.metricRegistry.meter("inputs")

  implicit val system: ActorSystem = ActorSystem.create()
  implicit val materializer: ActorMaterializer = ActorMaterializer.create(system)
  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

  // -- Test Random generator
  println(MyRandom.generateCourseBoardingStart().asJson.noSpaces)
  println(MyRandom.generateCourseBoardingEnd().asJson.noSpaces)
  println(MyRandom.generateTicketScan().asJson.noSpaces)
  println("==================================================")

  // -- JMX FEATURE FLIP
  val mBeanServer = ManagementFactory.getPlatformMBeanServer
  val generationActivation = new Feature(true)
  generationActivation.registerBean(mBeanServer, generationActivation, "GenerationActivation")
  val useWS = new Feature(true)
  useWS.registerBean(mBeanServer, useWS, "UseWS")

  val streamDecider: Function[Throwable, Directive] = (e: Throwable) => {
    println(s"Decider: ${e.getMessage}", e)
    Supervision.stop
  }

  val wsClient = StandaloneAhcWSClient()

  Source.repeat(NotUsed)
    .map(_ => MyRandom.generateLine())
    .filter(_ => generationActivation.getActive)
    .idleTimeout(2 seconds)
    .recover({
      case NonFatal(ex) =>
        println(s"======> Flow recovered : $ex", ex)
        "\n"
        System.exit(1)
    })
    .map(e => {
      inputsMeter.mark()
      e
    })
    .mapAsync(10)(e => {
      //println("========= BEGIN ====")
      //println(e)
      if (useWS.getActive) {
        ScalaClient.call(wsClient)
      } else {
        Future.successful(0)
      }
    })
    .withAttributes(ActorAttributes.withSupervisionStrategy(streamDecider))
    .runWith(Sink.ignore)

  sys.addShutdownHook({
    wsClient.close()
    system.terminate()
    Metrics.closeMetrics()
    println("========> Application terminated")
  })
}
