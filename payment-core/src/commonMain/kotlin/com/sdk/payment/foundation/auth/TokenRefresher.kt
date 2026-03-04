package com.sdk.payment.foundation.auth

import com.sdk.payment.core.auth.TokenProvider
import com.sdk.payment.foundation.auth.TokenPair

class TokenRefresher(
    private val config: AuthConfig,
    private val tokenProvider: TokenProvider
) {

    suspend fun refreshIfNeeded() {
        val tokens = tokenProvider.getTokenPair()

        if (tokens.isExpired()) {
            tokenProvider.refreshToken(
                config.refreshUrl,
                config.clientId,
                config.clientSecret
            )
        }
    }
}