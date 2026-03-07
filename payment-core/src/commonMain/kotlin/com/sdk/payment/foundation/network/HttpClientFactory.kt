package com.sdk.payment.core.network

import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.foundation.network.SignaturePlugin
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.Json

object HttpClientFactory {
    fun create(
        engine: HttpClientEngine,
        config: PaymentConfig,
    ): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            install(DefaultRequest) {
                url(config.baseUrl)
                header("x-version",config.apiVersion)
                header("response-type", "url")
                header("Content-Type", "application/json")
                val authString = "${config.merchantId}:${config.secretUnbound}"
                val encodedAuth = authString.toByteArray().encodeBase64()
                header("Authorization", "Basic $encodedAuth")
            }
            install(SignaturePlugin(config.hashKey))
        }
    }
}