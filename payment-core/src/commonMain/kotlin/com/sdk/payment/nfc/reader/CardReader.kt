package com.sdk.payment.nfc.reader

import com.sdk.payment.nfc.NfcResult

interface CardReader {
    suspend fun readCard(): NfcResult
}