package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentResult(
    val transactionId: String,
    val status: PaymentStatus
)