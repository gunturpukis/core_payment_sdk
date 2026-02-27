package com.sdk.payment.core

data class PaymentConfig(
    val baseUrl: String,
    val timeoutMillis: Long = 30_000
)