package com.sdk.payment.nfc.utlis

object HexUtils {

    fun hexToBytes(hex: String): ByteArray {
        val clean = hex.replace(" ", "")
        val data = ByteArray(clean.length / 2)

        for (i in data.indices) {
            val index = i * 2
            data[i] = clean.substring(index, index + 2).toInt(16).toByte()
        }
        return data
    }

    fun bytesToHex(bytes: ByteArray): String {
        return bytes.joinToString("") { "%02X".format(it) }
    }
}