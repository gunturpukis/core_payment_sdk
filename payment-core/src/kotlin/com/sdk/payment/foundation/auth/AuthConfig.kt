package com.sdk.payment.foundation.auth

data class AuthConfig(
    val clientId: String,
    val clientSecret: String,
    val refreshUrl: String
)