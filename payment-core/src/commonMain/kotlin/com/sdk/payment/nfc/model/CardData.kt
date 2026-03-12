package com.sdk.payment.nfc.model

data class CardData(
    val cardNumber: String?,
    val expiryDate: String?,
    val cardType: String?
)