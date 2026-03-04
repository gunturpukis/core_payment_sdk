package com.sdk.payment.core.network

import com.sdk.payment.core.PaymentConfig
import com.sdk.payment.core.auth.TokenProvider
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.header
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.toByteArray
import korlibs.crypto.HMAC
import korlibs.crypto.Hash
import kotlin.io.encoding.Base64

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

            // ✅ Bearer Auth
            install(Auth) {
                bearer {

                    loadTokens {
                        val token = tokenProvider.getTokenPair()
                        BearerTokens(
                            token.accessToken,
                            token.refreshToken
                        )
                    }

                    refreshTokens {
                        tokenProvider.refreshToken(
                            config.refreshUrl,
                            config.clientId,
                            config.clientSecret
                        )

                        val newToken = tokenProvider.getTokenPair()
                        BearerTokens(
                            newToken.accessToken,
                            newToken.refreshToken
                        )
                    }
                }
            }

            // ✅ Default Headers (x-version + signature)
                val SignaturePlugin = createClientPlugin("SignaturePlugin") {

                    onRequest { request, _ ->

                        val payload = "${request.method.value}:${request.url.encodedPath}:"

                        val hash = HMAC.hmacSHA256(
                            key = config.clientSecret.encodeToByteArray(),
                            data = payload.encodeToByteArray()
                        )
                        val bite: ByteArray = hash.bytes

                        request.headers.append("x-req-signature", Base64.encode(bite))
                    }
                }
            install(SignaturePlugin)

        }
}