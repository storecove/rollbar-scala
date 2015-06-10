package com.github.acidghost.rollbar.appenders

import com.github.acidghost.rollbar.util.FiniteQueue
import com.github.acidghost.rollbar.{RollbarNotifierDefaults, RollbarNotifier, RollbarNotifierFactory}
import org.slf4j.MDC

import scala.collection.JavaConversions._
import scala.collection.{immutable, mutable}

/**
 * Created by andrea on 08/06/15.
 */
trait AbstractAppender {

    protected val DEFAULT_LOGS_LIMITS = 100

    protected var enabled: Boolean = true
    protected var onlyThrowable: Boolean = true

    protected var url: String = RollbarNotifierDefaults.defaultUrl
    protected var apiKey: String = _
    protected var environment: String = _
    protected var notifyLevelString: String = "ERROR"
    protected var limit: Int = DEFAULT_LOGS_LIMITS

    protected val rollbarNotifier: RollbarNotifier = RollbarNotifierFactory.getNotifier(apiKey, environment)

    protected val logBuffer: FiniteQueue[String] = new FiniteQueue[String](immutable.Queue[String]())

    def setNotifyLevel(level: String): Unit

    protected def notifyLevel: Any = "ERROR"

    def setEnabled(enabled: Boolean): Unit = this.enabled = enabled

    def setOnlyThrowable(onlyThrowable: Boolean): Unit = this.onlyThrowable = onlyThrowable

    def setApiKey(apiKey: String): Unit = {
        this.apiKey = apiKey
        rollbarNotifier.setApiKey(apiKey)
    }

    def setEnvironment(environment: String): Unit = {
        this.environment = environment
        rollbarNotifier.setEnvironment(environment)
    }

    def setUrl(url: String): Unit = {
        this.url = url
        rollbarNotifier.setUrl(url)
    }

    def setLimit(limit: Int): Unit = this.limit = limit

    def getEnabled: Boolean = enabled
    def getOnlyThrowable: Boolean = onlyThrowable
    def getApiKey: String = apiKey
    def getEnvironment: String = environment
    def getUrl: String = url
    def getNotifyLevel: String = notifyLevelString
    def getLimit: Int = limit

    protected def getMDCContext: mutable.Map[String, String] = {
        val mdc = MDC.getCopyOfContextMap
        if (mdc == null) {
            mutable.Map.empty[String, String]
        } else {
            mapAsScalaMap(mdc)
        }
    }

}
