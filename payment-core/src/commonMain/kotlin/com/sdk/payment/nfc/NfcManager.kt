package com.sdk.payment.nfc

expect class NfcManager {
    fun startScan()
    fun stopScan()
    fun setListener(listener: NfcListener)
}

interface NfcListener {
    fun onTagDiscovered(tag: NfcTag)
    fun onError(error: String)
}