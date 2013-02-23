package org.bigsaas.util

import scala.concurrent.duration.Duration
import scala.concurrent.Awaitable

package object concurrent extends ConcurrentUtils {
}

trait ConcurrentUtils {
  def waitFor[A](duration : Duration)(f : => Awaitable[A]) = 
    scala.concurrent.Await.result(f, duration)
}
