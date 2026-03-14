package com.sdk.payment.nfc

import android.nfc.tech.IsoDep

class EmvReader(
    private val isoDep: IsoDep
) {
    fun read(): NfcResult {
        val selectPpse = byteArrayOf(
            0x00,0xA4.toByte(),0x04,0x00,
            0x0E,
            0x32,0x50,0x41,0x59,0x2E,0x53,0x59,0x53,
            0x2E,0x44,0x44,0x46,0x30,0x31,
            0x00
        )
        val response = isoDep.transceive(selectPpse)
        val parser = EmvParser()
        return parser.parse(response)
    }
}