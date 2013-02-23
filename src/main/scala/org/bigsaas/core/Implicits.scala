package org.bigsaas.core

trait Implicits {

  /**
   * Automatically treat value as Some(value). Useful for optional function arguments.
   */
  implicit def toOption[A](a: A) = Option(a)

}

package object implicits extends Implicits