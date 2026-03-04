package com.sdk.payment.foundation.auth

import com.sdk.payment.foundation.time.currentTimeMillis

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
) {
    fun isExpired(): Boolean =
        currentTimeMillis() >= expiresAt
}