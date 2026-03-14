package com.sdk.payment.nfc

class EmvParser {
    fun parse(data: ByteArray): NfcResult {
        val hex = bytesToHex(data)
        val cardNumber = extract(hex, "5A")
        val expiry = extract(hex, "5F24")
        val holder = extract(hex, "5F20")

        return NfcResult(
            cardNumber = cardNumber,
            expiryDate = expiry,
            cardHolder = holder,
            cardType = detectType(cardNumber)
        )
    }
    private fun extract(hex: String, tag: String): String? {
        val index = hex.indexOf(tag)
        if (index == -1) return null
        val length = hex.substring(index + tag.length, index + tag.length + 2).toInt(16)
        return hex.substring(
            index + tag.length + 2,
            index + tag.length + 2 + (length * 2)
        )
    }

    private fun detectType(pan: String?): String? {
        if (pan == null) return null
        return when {
            pan.startsWith("4") -> "VISA"
            pan.startsWith("5") -> "MASTERCARD"
            pan.startsWith("34") -> "AMEX"
            else -> "UNKNOWN"
        }
    }
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789ABCDEF"
        val result = StringBuilder(bytes.size * 2)
        for (byte in bytes) {
            val i = byte.toInt() and 0xFF
            result.append(hexChars[i shr 4])
            result.append(hexChars[i and 0x0F])
        }
        return result.toString()
    }
}