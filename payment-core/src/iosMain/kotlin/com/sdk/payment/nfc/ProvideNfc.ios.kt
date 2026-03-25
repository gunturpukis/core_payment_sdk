package com.sdk.payment.nfc

actual fun provideNfcManager(): NfcManager {
    return NfcManager()
}