package com.github.acidghost.rollbar

import org.json4s.JValue
import org.slf4j.{LoggerFactory, Logger}

import scala.collection.mutable

/**
 * Created by acidghost on 06/06/15.
 */
trait RollbarNotifier extends RollbarNotifierData {

    protected val defaultPlatform = "JVM"
    protected val notifierName = "rollbar-scala"
    protected val notifierVersion = "0.0.1"

    protected val logger: Logger = LoggerFactory.getLogger(classOf[RollbarNotifier])

    def getUrl: String = url
    def getApiKey: String = apiKey
    def getEnvironment: String = environment
    def getLanguage: String = language

    def setUrl(url: String) = this.url = url
    def setApiKey(apiKey: String) = this.apiKey = apiKey
    def setEnvironment(environment: String) = this.environment = environment
    def setLanguage(language: String) = this.language = language

    def notify(level: String, message: String, throwable: Option[Throwable], mdc: mutable.Map[String, String]): JValue

}

abstract case class RollbarNotifierData(protected var url: String, protected var apiKey: String, protected var environment: String, protected var language: String)
