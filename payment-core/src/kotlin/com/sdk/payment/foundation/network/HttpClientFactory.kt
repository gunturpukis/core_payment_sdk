package com.sdk.payment.core.network

import com.sdk.payment.core.PaymentConfig
import com.sdk.payment.core.auth.TokenProvider
import com.sdk.payment.foundation.auth.TokenRefresher
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

object HttpClientFactory {

    fun create(
        engine: HttpClientEngine,
        config: PaymentConfig,
        tokenProvider: TokenProvider
    ): HttpClient =
        HttpClient(engine) {

            install(ContentNegotiation) {
                json()
            }

            install(Auth) {
                bearer(
                    { TokenRefresher(tokenProvider).refreshIfNeeded() }
                )
            }
        }
}