package com.example.sarwan.renkar.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

object HashUtility {
    fun md5New(input: String): String {
        var output = input
        try {

            val digest = MessageDigest.getInstance("MD5")
            digest.reset()
            digest.update(output.toByteArray())
            val outDigest = digest.digest()
            val outBigInt = BigInteger(1, outDigest)
            output = outBigInt.toString(16)
            while (output.length < 32) {
                output = "0$output"
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }

        return output.toLowerCase()
    }

}