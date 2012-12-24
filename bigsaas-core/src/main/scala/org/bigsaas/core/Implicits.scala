package org.bigsaas.core

trait Implicits {
		implicit def toOption[A](a : A) = Option(a)
}

package object implicits extends Implicits