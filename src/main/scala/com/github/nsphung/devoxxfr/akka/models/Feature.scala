package com.github.nsphung.devoxxfr.akka.models

import javax.management.{MBeanServer, ObjectName}
import org.slf4j.LoggerFactory

import scala.beans.BeanProperty

trait FeatureMBean {
  def getActive(): Boolean

  def setActive(d: Boolean): Unit

  def registerBean(beanServer: MBeanServer, feature: Feature, featureName: String): Unit = {
    val beanPath = s"${Feature.JMX_FEATURE_PREFIX}${featureName}"
    val objectName = ObjectName.getInstance(beanPath)
    if (!beanServer.isRegistered(objectName)) {
      Feature.LOGGER.info(s"JMX Register FEATURE FLIP bean: ${beanPath} with default value '${feature.active}'")
      beanServer.registerMBean(feature, objectName)
    }
  }
}

class Feature(@BeanProperty var active: Boolean) extends FeatureMBean

object Feature {
  val JMX_FEATURE_PREFIX: String = "reactive.jammed.architecture:type=Features,subtype="
  val LOGGER = LoggerFactory.getLogger(Feature.getClass)
}