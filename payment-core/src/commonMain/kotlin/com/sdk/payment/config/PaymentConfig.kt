package com.sdk.payment.config

data class PaymentConfig(
    val baseUrl: String,
    val merchantId: String,
    val timeoutMillis: Long = 30_000,
    val secretUnbound: String,
    val hashKey: String,
//    val refreshUrl: String,
    val apiVersion: String = "v3",
//    val externalId: String,
//    val orderId: String,
) {

}