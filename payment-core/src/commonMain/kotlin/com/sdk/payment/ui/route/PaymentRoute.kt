package com.sdk.payment.ui.route

import kotlinx.serialization.Serializable

@Serializable
internal data class PaymentRoute(
    val credentialToken: String,
    val paymentData: String
)