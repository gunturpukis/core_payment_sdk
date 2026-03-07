package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PaymentRequest(
    @SerialName("external_id")
    val externalId: String,
    @SerialName("order_id")
    val orderId: String,
    val currency: String,
    @SerialName("payment_method")
    val paymentMethod: String? = null,
    @SerialName("payment_channel")
    val paymentChannel: String? = null,
    @SerialName("payment_mode")
    val paymentMode: String? = null,
    @SerialName("payment_details")
    val paymentDetails: PaymentDetails,
    @SerialName("customer_details")
    val customerDetails: CustomerDetails? = null,
    @SerialName("card_details")
    val cardDetails: CardDetails? = null,
    @SerialName("return_url")
    val returnUrl: String? = null,
    @SerialName("callback_url")
    val callbackUrl: String? = null
)

@Serializable
data class PaymentDetails(
    val amount: Long?,
    @SerialName("transaction_description")
    val transactionDescription: String? = null
)

@Serializable
data class CardDetails(
    @SerialName("card_number")
    val cardNumber: String,
    @SerialName("card_expired_month")
    val cardExpiredMonth: String,
    @SerialName("card_expired_year")
    val cardExpiredYear: String,
    @SerialName("card_cvn")
    val cardCvn: String,
    @SerialName("card_holder_name")
    val cardHolderName: String
)