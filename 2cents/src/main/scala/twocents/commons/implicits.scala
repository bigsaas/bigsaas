package twocents.commons

package object implicits {
	implicit def toOption[A](a : A) = Option(a)
}