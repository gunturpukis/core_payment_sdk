package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentStatus {
    SUCCESS,
    FAILED,
    PENDING
}