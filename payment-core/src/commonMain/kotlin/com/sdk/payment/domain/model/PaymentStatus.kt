package com.sdk.payment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentStatus {
    @SerialName("SUCCESS")
    SUCCESS,
    @SerialName("PENDING")
    PENDING,
    @SerialName("FAILED")
    FAILED
}