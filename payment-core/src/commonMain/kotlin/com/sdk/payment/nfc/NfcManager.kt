package com.sdk.payment.nfc

expect class NfcManager {
    fun startScan()
    fun stopScan()
    fun setListener(listener: (NfcResult) -> Unit)
}