package com.sdk.payment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResponse(
    @SerialName("response_code")
    val responseCode: String,
    @SerialName("response_message")
    val responseMessage: String,
    @SerialName("data")
    val data: PaymentResult?
)

@Serializable
data class PaymentResult(
    @SerialName("link")
    val link: String? = null,
)