package com.sdk.payment.nfc

import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.sdk.payment.nfc.model.CardData
import com.sdk.payment.nfc.utlis.HexUtils

object EmvNfcReader {

    fun readCard(tag: Tag): NfcTag? {
        val isoDep = IsoDep.get(tag) ?: return null
        try {
            isoDep.connect()
            // SELECT PPSE
            val ppse = isoDep.transceive(
                HexUtils.hexToBytes("00A404000E325041592E5359532E444446303100")
            )
            val aid = extractAid(ppse) ?: return null
            // SELECT AID
            isoDep.transceive(selectAid(aid))
            // READ RECORD
            val record = isoDep.transceive(
                HexUtils.hexToBytes("00B2010C00")
            )
            val pan = extractPan(record)
            val expiry = extractExpiry(record)
            val type = detectCardType(aid)
            return NfcTag(
                id = tag.id.joinToString("") { "%02X".format(it) },
                payload = record
//                cardNumber = pan,
//                expiryDate = expiry,
//                cardType = type
            )

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isoDep.close()
        }
        return null
    }

    private fun selectAid(aid: String): ByteArray {
        val aidLength = "%02X".format(aid.length / 2)
        return HexUtils.hexToBytes("00A40400$aidLength$aid")
    }

    private fun extractAid(data: ByteArray): String? {
        val hex = HexUtils.bytesToHex(data)
        val index = hex.indexOf("4F")
        if (index == -1) return null
        val length = hex.substring(index + 2, index + 4).toInt(16) * 2
        return hex.substring(index + 4, index + 4 + length)
    }

    private fun extractPan(data: ByteArray): String? {
        val hex = HexUtils.bytesToHex(data)
        val index = hex.indexOf("57")
        if (index == -1) return null
        val length = hex.substring(index + 2, index + 4).toInt(16) * 2
        val track2 = hex.substring(index + 4, index + 4 + length)
        val pan = track2.substringBefore("D")
        return pan.chunked(4).joinToString(" ")
    }

    private fun extractExpiry(data: ByteArray): String? {
        val hex = HexUtils.bytesToHex(data)
        val index = hex.indexOf("57")
        if (index == -1) return null
        val length = hex.substring(index + 2, index + 4).toInt(16) * 2
        val track2 = hex.substring(index + 4, index + 4 + length)
        val expiry = track2.substringAfter("D").take(4)
        val year = expiry.take(2)
        val month = expiry.takeLast(2)
        return "$month/$year"
    }

    private fun detectCardType(aid: String): String {
        return when {
            aid.startsWith("A000000003") -> "VISA"
            aid.startsWith("A000000004") -> "MASTERCARD"
            aid.startsWith("A000000025") -> "AMEX"
            else -> "UNKNOWN"
        }
    }
}