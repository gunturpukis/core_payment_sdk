package com.sdk.payment.nfc

import android.nfc.tech.IsoDep

class AndroidIsoDepTransceiver(
    private val isoDep: IsoDep
) : NfcTransceiver {
    override fun transceive(command: ByteArray): ByteArray {
        return isoDep.transceive(command)
    }
}