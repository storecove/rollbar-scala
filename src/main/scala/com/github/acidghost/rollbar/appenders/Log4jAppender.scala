package com.github.acidghost.rollbar.appenders

import org.apache.log4j.AppenderSkeleton
import org.apache.log4j.spi.LoggingEvent

/**
 * Created by acidghost on 08/06/15.
 */
class Log4jAppender extends AppenderSkeleton {

    override def append(event: LoggingEvent): Unit = ???

    override def requiresLayout(): Boolean = ???

    override def close(): Unit = ???
}
