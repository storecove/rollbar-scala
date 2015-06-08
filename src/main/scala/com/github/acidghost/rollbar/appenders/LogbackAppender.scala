package com.github.acidghost.rollbar.appenders

import ch.qos.logback.classic.spi.{ILoggingEvent, ThrowableProxy}
import ch.qos.logback.core.UnsynchronizedAppenderBase

/**
 * Created by acidghost on 08/06/15.
 */
class LogbackAppender extends UnsynchronizedAppenderBase[ILoggingEvent] with AbstractAppender {

    override def append(event: ILoggingEvent): Unit = {
        rollbarNotifier.notify(event.getLevel.toString, event.getFormattedMessage, getThrowable(event), getMDCContext)
    }

    override def start(): Unit = {
        if (apiKey == null || apiKey.isEmpty) {
            addError("No apiKey set for the appender named [" + getName + "].")
        } else if (environment == null || environment.isEmpty) {
            addError("No environment set for the appender named [" + getName + "].")
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
}
