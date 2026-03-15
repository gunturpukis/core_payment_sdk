package com.sdk.payment.nfc

interface NfcTransceiver {
    fun transceive(command: ByteArray): ByteArray
}