package com.sdk.payment.ui.model

data class CardState(
    val cardNumber: String = "",
    val cardHolder: String = "",
    val expiry: String = "",
    val cvv: String = "",
    val cardBrand: String = "",
    val isCardFlipped: Boolean = false,
    val showNfcSheet: Boolean = false,
    val showCvvInfo: Boolean = false
)