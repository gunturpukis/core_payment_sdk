package com.sdk.payment.core.auth

import com.sdk.payment.foundation.auth.TokenPair
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TokenProvider(
    private val client: HttpClient
) {
    private var tokenPair: TokenPair? = null
    fun getTokenPair(): TokenPair {
        return tokenPair ?: error("Token not initialized")
    }
    suspend fun refreshToken(
        refreshUrl: String,
        clientId: String,
        clientSecret: String
    ) {
        val response: Map<String, String> = client.post(refreshUrl) {
            setBody(
                mapOf(
                    "client_id" to clientId,
                    "client_secret" to clientSecret,
                    "grant_type" to "refresh_token"
                )
            )
        }.body()
        tokenPair = TokenPair(
            accessToken = response["access_token"]!!,
            refreshToken = response["refresh_token"]!!,
            expiresAt = response["expires_at"]!!.toLong()
        )
    }
    fun setToken(token: TokenPair) {
        tokenPair = token
    }
}