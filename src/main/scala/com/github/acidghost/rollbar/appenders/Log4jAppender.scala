package com.github.acidghost.rollbar.appenders

import com.github.acidghost.rollbar.util.{FiniteQueue, _}
import com.github.acidghost.rollbar.{RollbarNotifierFactory, RollbarNotifier}
import org.apache.log4j.helpers.LogLog
import org.apache.log4j.{Level, AppenderSkeleton}
import org.apache.log4j.spi.{ThrowableInformation, LoggingEvent}
import org.slf4j.MDC

import scala.collection.immutable
import scala.collection.mutable
import scala.collection.JavaConversions._

/**
 * Created by acidghost on 08/06/15.
 */
class Log4jAppender extends AppenderSkeleton {

    protected val DEFAULT_LOGS_LIMITS = 100

    protected var enabled: Boolean = true
    protected var onlyThrowable: Boolean = true

    protected var apiKey: String = _
    protected var environment: String = _
    protected var notifyLevel: Level = Level.ERROR
    protected var limit: Int = DEFAULT_LOGS_LIMITS

    protected val logBuffer: FiniteQueue[String] = new FiniteQueue[String](immutable.Queue[String]())

    protected val rollbarNotifier: RollbarNotifier = RollbarNotifierFactory.getNotifier(apiKey, environment)

    override def append(event: LoggingEvent): Unit = {
        if (enabled) {
            try {
                logBuffer.enqueueFinite(this.layout.format(event).trim, limit)

                if (event.getLevel.isGreaterOrEqual(notifyLevel)) {
                    val hasThrowable = event.getThrowableInformation != null || event.getMessage.isInstanceOf[Throwable]
                    if (onlyThrowable && !hasThrowable) return

                    rollbarNotifier.notify(event.getLevel.toString, event.getMessage.toString, getThrowable(event), getContext)
                }
            } catch {
                case e: Exception => LogLog.error("Error sending error notification! error=" + e.getClass.getName + " with message=" + e.getMessage)
            }
        }
    }

    override def requiresLayout(): Boolean = true

    override def close(): Unit = {}

    protected def getContext: mutable.Map[String, String] = {
        val mdc = MDC.getCopyOfContextMap
        if (mdc == null) {
            mutable.Map.empty[String, String]
        } else {
            mapAsScalaMap(mdc)
        }
    }

    protected def getThrowable(event: LoggingEvent): Option[Throwable] = {
        event.getThrowableInformation match {
            case throwableInfo: ThrowableInformation => Some(throwableInfo.getThrowable)
            case _ => event.getMessage match {
                case throwable: Throwable => Some(throwable)
                case throwable: String => Some(throwable.asInstanceOf[Throwable])
                case _ => None
            }
        }
    }

    def setEnabled(enabled: Boolean) = this.enabled = enabled

    def setOnlyThrowable(onlyThrowable: Boolean) = this.onlyThrowable = onlyThrowable

    def setApiKey(apiKey: String) = rollbarNotifier.setApiKey(apiKey)

    def setEnvironment(environment: String) = rollbarNotifier.setEnvironment(environment)

    def setUrl(url: String) = rollbarNotifier.setUrl(url)

    def setNotifyLevel(level: String) = this.notifyLevel = Level.toLevel(level, this.notifyLevel)

    def setLimit(limit: Int) = this.limit = limit

}
