package com.sdk.payment.core.auth

import com.sdk.payment.foundation.auth.TokenPair

interface TokenProvider {

    suspend fun getTokenPair(): TokenPair

//    suspend fun getRefreshToken(): String?

    suspend fun refreshToken(
        refreshUrl: String,
        clientId: String,
        clientSecret: String
    )
}