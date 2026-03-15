package com.sdk.payment.nfc


data class NfcResult(
    val cardNumber: String?,
    val expiryDate: String?,
    val cardHolder: String?,
    val cardType: String?
)