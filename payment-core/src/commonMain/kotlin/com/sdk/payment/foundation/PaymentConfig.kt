package com.sdk.payment.core

data class PaymentConfig(
    val baseUrl: String,
    val clientId: String,
    val timeoutMillis: Long = 30_000,
    val clientSecret: String,
    val refreshUrl: String,
    val apiVersion: String = "v3"
) {

}