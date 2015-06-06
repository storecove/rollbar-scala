package com.github.acidghost.rollbar

/**
 * Created by acidghost on 06/06/15.
 */
object RollbarNotifierFactory {

    def getNotifier(apiKey: String,
                    environment: String,
                    language: String = "scala",
                    url: String = "https://api.rollbar.com/api/1/item/"): RollbarNotifier = new RollbarNotifierImpl(url, apiKey, environment, language)

}
