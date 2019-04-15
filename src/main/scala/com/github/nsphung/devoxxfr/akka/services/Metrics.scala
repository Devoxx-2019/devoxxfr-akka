package com.github.nsphung.devoxxfr.akka.services

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.codahale.metrics.graphite.{GraphiteReporter, PickledGraphite}
import com.codahale.metrics.{ConsoleReporter, MetricRegistry}

object Metrics {
  val metricRegistry: MetricRegistry = new MetricRegistry()

  def startMetrics(): Unit = {
    startConsoleReporter()
    startGraphiteReporter()
  }

  def closeMetrics(): Unit = {
    consoleReporter.close()
    graphiteReporter.close()
    pickledGraphite.close()
  }

  private val consoleReporter: ConsoleReporter = ConsoleReporter
    .forRegistry(metricRegistry)
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .build

  private val pickledGraphite: PickledGraphite = new PickledGraphite(new InetSocketAddress("localhost", 2004))
  private val graphiteReporter: GraphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
    .prefixedWith("reactive.jammed.architecture")
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .build(pickledGraphite)

  private def startConsoleReporter(): Unit = {
    //consoleReporter.start(5, TimeUnit.SECONDS)
  }

  private def startGraphiteReporter(): Unit = {
    graphiteReporter.start(1, TimeUnit.SECONDS)
  }
}
