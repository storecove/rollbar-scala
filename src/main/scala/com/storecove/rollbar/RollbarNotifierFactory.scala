package com.storecove.rollbar

/**
 * Created by acidghost on 06/06/15.
 */
object RollbarNotifierFactory {

    def getNotifier(apiKey: String,
                    environment: String,
                    language: String = RollbarNotifierDefaults.defaultLanguage,
                    url: String = RollbarNotifierDefaults.defaultUrl,
                    platform: String = RollbarNotifierDefaults.defaultPlatform): RollbarNotifier = new RollbarNotifierImpl(url, apiKey, environment, language, platform)

}

object RollbarNotifierDefaults {
    val defaultPlatform = "JVM"
    val defaultLanguage = "scala"
    val defaultUrl = "https://api.rollbar.com/api/1/item/"
}
