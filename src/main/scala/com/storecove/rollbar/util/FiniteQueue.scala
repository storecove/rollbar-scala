package com.storecove.rollbar.util

import scala.collection.immutable.Queue

/**
 * Created by andrea on 08/06/15.
 */
class FiniteQueue[A](q: Queue[A]) {

  def enqueueFinite[B >: A](elem: B, maxSize: Int): Queue[B] = {
    var ret = q.enqueue(elem)
    while (ret.size > maxSize) { ret = ret.dequeue._2 }
    ret
  }
}
