package com.github.acidghost.rollbar.test

import com.github.acidghost.rollbar.RollbarNotifierFactory
import org.scalatest.{Matchers, FlatSpec}
import org.slf4j.{LoggerFactory, MDC}
import scala.collection.JavaConversions._

/**
 * Created by acidghost on 06/06/15.
 */
class RollbarNotifierSpec extends FlatSpec with Matchers {

    val apiKey = "FAKE_API_KEY"
    val environment = "test"
    val language = "java"
    val url = "http://www.fakeurl.com/api/v2"
    val defaultLanguage = "scala"
    val defaultUrl = "https://api.rollbar.com/api/1/item/"

    val logger = LoggerFactory.getLogger(getClass)

    "A RollbarNotifier" should "return the right parameters" in {
        val notifier = RollbarNotifierFactory.getNotifier(apiKey, environment)
        notifier.getUrl should be (defaultUrl)
        notifier.getApiKey should be (apiKey)
        notifier.getEnvironment should be (environment)
        notifier.getLanguage should be (defaultLanguage)
        val notifier2 = RollbarNotifierFactory.getNotifier(apiKey, environment, language = language, url = url)
        notifier2.getLanguage should be (language)
        notifier2.getUrl should be (url)
    }

    it should "notify to rollbar correctly" in {
        val notifier = RollbarNotifierFactory.getNotifier(apiKey, environment)
        MDC.put("test_MDC_key", "test value")
        val stream = new java.io.ByteArrayOutputStream()
        Console.withOut(stream) {
            notifier.notify("info", "messagetest", None, mapAsScalaMap(MDC.getCopyOfContextMap))
        }
        stream.toString should be ("test value\n")
    }

}
