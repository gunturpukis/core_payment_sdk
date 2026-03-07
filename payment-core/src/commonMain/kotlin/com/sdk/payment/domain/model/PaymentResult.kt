package com.sdk.payment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentResult(
    @SerialName("status")
    val status: PaymentStatus,
    @SerialName("external_id")
    val externalId: String? = null,
    @SerialName("order_id")
    val orderId: String? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("redirect_url")
    val redirectUrl: String? = null,
    @SerialName("transaction_id")
    val transactionId: String? = null
)