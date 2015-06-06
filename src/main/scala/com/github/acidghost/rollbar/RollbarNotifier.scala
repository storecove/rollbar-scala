package com.github.acidghost.rollbar

/**
 * Created by acidghost on 06/06/15.
 */
trait RollbarNotifier {

    def getUrl: String
    def getApiKey: String
    def getEnvironment: String
    def getLanguage: String

    def notify(message: String, throwable: Throwable, mdcGet: (String) => String)

}

private class RollbarNotifierImpl(protected val url: String,
                                  protected val apiKey: String,
                                  protected val environment: String,
                                  protected val language: String) extends RollbarNotifier {

    def getUrl: String = url
    def getApiKey: String = apiKey
    def getEnvironment: String = environment
    def getLanguage: String = language

    override def notify(message: String, throwable: Throwable, mdcGet: (String) => String): Unit = {
        val name = mdcGet("test_MDC_key")
        println(name)
    }
}
