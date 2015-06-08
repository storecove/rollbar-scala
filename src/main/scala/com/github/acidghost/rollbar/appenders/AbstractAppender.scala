package com.github.acidghost.rollbar.appenders

import com.github.acidghost.rollbar.{RollbarNotifierFactory, RollbarNotifier}
import com.github.acidghost.rollbar.util.FiniteQueue
import org.apache.log4j.Level
import org.slf4j.MDC

import scala.collection.JavaConversions._
import scala.collection.{mutable, immutable}

/**
 * Created by andrea on 08/06/15.
 */
trait AbstractAppender {

    protected val DEFAULT_LOGS_LIMITS = 100

    protected var enabled: Boolean = true
    protected var onlyThrowable: Boolean = true

    protected var apiKey: String = _
    protected var environment: String = _
    protected var notifyLevelString: String = "ERROR"
    protected var limit: Int = DEFAULT_LOGS_LIMITS

    protected val logBuffer: FiniteQueue[String] = new FiniteQueue[String](immutable.Queue[String]())

    protected val rollbarNotifier: RollbarNotifier = RollbarNotifierFactory.getNotifier(apiKey, environment)

    def setEnabled(enabled: Boolean) = this.enabled = enabled

    def setOnlyThrowable(onlyThrowable: Boolean) = this.onlyThrowable = onlyThrowable

    def setApiKey(apiKey: String) = rollbarNotifier.setApiKey(apiKey)

    def setEnvironment(environment: String) = rollbarNotifier.setEnvironment(environment)

    def setUrl(url: String) = rollbarNotifier.setUrl(url)

    def setNotifyLevel(level: String): Unit

    def setLimit(limit: Int) = this.limit = limit
    protected def notifyLevel: Any = "ERROR"

    protected def getMDCContext: mutable.Map[String, String] = {
        val mdc = MDC.getCopyOfContextMap
        if (mdc == null) {
            mutable.Map.empty[String, String]
        } else {
            mapAsScalaMap(mdc)
        }
    }

}
