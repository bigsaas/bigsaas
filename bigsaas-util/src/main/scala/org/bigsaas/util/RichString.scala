package org.bigsaas.util

import java.security.MessageDigest

trait Implicits {
	implicit class RichString(s: String)  {
	  def sha256Hash: Array[Byte] = {
	    val digest = MessageDigest.getInstance("SHA-256")
	    digest.update(s.getBytes("UTF-8"))
	    digest.digest
	  }
	}
}

package object implicits extends Implicits
