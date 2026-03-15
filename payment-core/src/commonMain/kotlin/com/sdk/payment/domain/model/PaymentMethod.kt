package com.sdk.payment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod {
    @SerialName("CARD")
    CARD,
    @SerialName("EWALLET")
    EWALLET,
    @SerialName("VA")
    VA
}