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
    val data: PaymentResult
)

@Serializable
data class PaymentResult(
    @SerialName("link")
    val redirectUrl: String? = null,

//    // Field di bawah ini opsional/null jika tidak ada di JSON yang kamu berikan
//    @SerialName("external_id")
//    val externalId: String? = null,
//    @SerialName("order_id")
//    val orderId: String? = null,
//    @SerialName("transaction_id")
//    val transactionId: String? = null
)