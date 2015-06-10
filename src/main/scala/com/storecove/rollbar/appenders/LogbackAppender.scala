package com.storecove.rollbar.appenders

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.{ILoggingEvent, ThrowableProxy}
import ch.qos.logback.core.UnsynchronizedAppenderBase
import org.apache.log4j.helpers.LogLog

/**
 * Created by acidghost on 08/06/15.
 */
class LogbackAppender extends UnsynchronizedAppenderBase[ILoggingEvent] with AbstractAppender {

    override def append(event: ILoggingEvent): Unit = {
        if (enabled) {
            try {
                if (event.getLevel.isGreaterOrEqual(notifyLevel)) {
                    val hasThrowable = event.getThrowableProxy != null
                    if (onlyThrowable && !hasThrowable) return

                    rollbarNotifier.notify(event.getLevel.toString, event.getMessage, getThrowable(event), getMDCContext)
                }
            } catch {
                case e: Exception => LogLog.error("Error sending error notification! error=" + e.getClass.getName + " with message=" + e.getMessage)
            }
        }
    }

    override def start(): Unit = {
        if (this.apiKey == null || this.apiKey.isEmpty) {
            this.addError("No apiKey set for the appender named [" + getName + "].")
        } else if (this.environment == null || this.environment.isEmpty) {
            this.addError("No environment set for the appender named [" + getName + "].")
        } else {
            super.start()
        }
    }

    protected def getThrowable(event: ILoggingEvent): Option[Throwable] = {
        event.getThrowableProxy match {
            case throwableProxy: ThrowableProxy => Some(throwableProxy.getThrowable)
            case _ => None
        }
    }

    override def notifyLevel: Level = Level.toLevel(notifyLevelString)

    def setNotifyLevel(notifyLevel: String): Unit = notifyLevelString = notifyLevel

}
