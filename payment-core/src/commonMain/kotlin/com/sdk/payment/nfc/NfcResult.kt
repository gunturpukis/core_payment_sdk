package com.sdk.payment.nfc


sealed class NfcResult {
    data class Success(val tag: NfcTag) : NfcResult()
    data class Error(val message: String) : NfcResult()
}