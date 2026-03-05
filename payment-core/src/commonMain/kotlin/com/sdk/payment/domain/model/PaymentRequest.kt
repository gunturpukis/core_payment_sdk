package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val cardNumber: String,
    val cardHolder: String,
    val expiryDate: String,
    val cvv: String,
)