package com.sdk.payment.core.auth

import com.sdk.payment.foundation.auth.TokenPair

interface TokenProvider {

    suspend fun getAccessToken(): String?

    suspend fun getRefreshToken(): String?

    suspend fun refreshToken(clientId: String?, clientSecret: String?, refreshUrl: String?): TokenPair?
}