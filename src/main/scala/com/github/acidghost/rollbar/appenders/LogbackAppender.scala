package com.github.acidghost.rollbar.appenders

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.UnsynchronizedAppenderBase

/**
 * Created by acidghost on 08/06/15.
 */
class LogbackAppender extends UnsynchronizedAppenderBase[ILoggingEvent] {

    override def append(eventObject: ILoggingEvent): Unit = ???

    override def start(): Unit = super.start()
}
