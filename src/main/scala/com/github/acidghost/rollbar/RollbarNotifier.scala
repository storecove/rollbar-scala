package com.github.acidghost.rollbar

import org.json4s.JValue

import scala.collection.mutable

/**
 * Created by acidghost on 06/06/15.
 */
trait RollbarNotifier {

    protected val notifierName = "rollbar-scala"
    protected val notifierVersion = "0.0.1"

    protected var url: String = _
    protected var apiKey: String = _
    protected var environment: String = _
    protected var language: String = _
    protected var platform: String = _

    protected def log(x: Any) = println(s"[${classOf[RollbarNotifier]}] - $x")

    def getUrl: String = url
    def getApiKey: String = apiKey
    def getEnvironment: String = environment
    def getLanguage: String = language
    def getPlatform: String = platform

    def setUrl(url: String) = this.url = url
    def setApiKey(apiKey: String) = this.apiKey = apiKey
    def setEnvironment(environment: String) = this.environment = environment
    def setLanguage(language: String) = this.language = language
    def setPlatform(platform: String) = this.platform = platform

    def notify(level: String, message: String, throwable: Option[Throwable], mdc: mutable.Map[String, String]): JValue

}
