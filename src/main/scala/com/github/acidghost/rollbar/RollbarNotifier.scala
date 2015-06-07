package com.github.acidghost.rollbar

import org.json4s.JValue
import org.slf4j.{LoggerFactory, Logger}

import scala.collection.mutable

/**
 * Created by acidghost on 06/06/15.
 */
trait RollbarNotifier {

    protected val defaultPlatform = "JVM"
    protected val notifierName = "rollbar-scala"
    protected val notifierVersion = "0.0.1"

    protected val logger: Logger = LoggerFactory.getLogger(classOf[RollbarNotifier])

    def getUrl: String
    def getApiKey: String
    def getEnvironment: String
    def getLanguage: String

    def notify(level: String, message: String, throwable: Option[Throwable], mdc: mutable.Map[String, String]): JValue

}
