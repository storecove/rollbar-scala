package com.storecove.rollbar.test

import com.github.acidghost.rollbar.RollbarNotifierFactory
import com.storecove.rollbar.{RollbarNotifier, RollbarNotifierFactory}
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by acidghost on 06/06/15.
 */
class RollbarNotifierFactorySpec extends FlatSpec with Matchers {

    "A RollbarNotifierFactory" should "create a new RollbarNotifier" in {
        val notifier = RollbarNotifierFactory.getNotifier("FAKE_API_KEY", "test")
        notifier shouldBe an[RollbarNotifier]
    }

}
