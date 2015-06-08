package com.github.acidghost.rollbar.appenders

import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.helpers.LogLog
import org.apache.log4j.spi.{LoggingEvent, ThrowableInformation}

/**
 * Created by acidghost on 08/06/15.
 */
class Log4jAppender extends AppenderSkeleton with AbstractAppender {

    override def append(event: LoggingEvent): Unit = {
        if (enabled) {
            try {
                logBuffer.enqueueFinite(this.layout.format(event).trim, limit)

                if (event.getLevel.isGreaterOrEqual(notifyLevel)) {
                    val hasThrowable = event.getThrowableInformation != null || event.getMessage.isInstanceOf[Throwable]
                    if (onlyThrowable && !hasThrowable) return

                    rollbarNotifier.notify(event.getLevel.toString, event.getMessage.toString, getThrowable(event), getMDCContext)
                }
            } catch {
                case e: Exception => LogLog.error("Error sending error notification! error=" + e.getClass.getName + " with message=" + e.getMessage)
            }
        }
    }

    override def requiresLayout(): Boolean = true

    override def close(): Unit = {}

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

}
