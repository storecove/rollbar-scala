package com.storecove.rollbar.test

import java.util.Properties
import com.storecove.rollbar.RollbarNotifierFactory
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.{LoggerFactory, MDC}
import org.json4s.jackson.JsonMethods._

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * Created by acidghost on 06/06/15.
 */
class RollbarNotifierSpec extends FlatSpec with Matchers {

    val properties = new Properties()
    properties.load(Thread.currentThread().getContextClassLoader.getResourceAsStream("testing.properties"))

    val apiKey = properties.getProperty("API_KEY", "YOUR_API_KEY")
    val environment = properties.getProperty("ENVIRONMENT", "testing")
    val language = "java"
    val url = "http://www.fakeurl.com/api/v2"
    val defaultLanguage = "scala"
    val defaultUrl = "https://api.rollbar.com/api/1/item/"

    val logger = LoggerFactory.getLogger(getClass)

    {
        MDC.put("user", "acidghost")
        MDC.put("platform", "scala")
        MDC.put("environment", "test")
    }

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
        val response = notifier.notify("INFO", "This is a test error notification.", None, getMDC)
        logger.info(compact(response))
    }

    it should "print the correct data" in {
        val notifier = RollbarNotifierFactory.getNotifier(apiKey, environment)
        val response = notifier.notify("INFO", "This is a test error notification.", Some(new Exception("this is the exception")), getMDC)
        logger.info(compact(response))
        val response2 = notifier.notify("INFO", "This is a test error notification.", None, getMDC)
        logger.info(compact(response2))
    }

    private def getMDC = {
        val mdc = MDC.getCopyOfContextMap
        if (mdc == null) {
            mutable.Map.empty[String, String]
        } else {
            mapAsScalaMap(mdc)
        }
    }

}
