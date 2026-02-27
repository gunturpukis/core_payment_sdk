package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    val amount: Long,
    val currency: String
)