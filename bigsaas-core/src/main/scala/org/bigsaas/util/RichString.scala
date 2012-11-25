package org.bigsaas.util

import java.security.MessageDigest

class RichString(s: String) {
  def sha256Hash: Array[Byte] = {
    val digest = MessageDigest.getInstance("SHA-256")
    digest.update(s.getBytes("UTF-8"))
    digest.digest
  }
}

object Implicits {
  implicit def RichString(s : String) = new RichString(s)
}