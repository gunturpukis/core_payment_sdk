package com.sdk.payment.data.remote

import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResult
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*

class PaymentApi(
    private val client: HttpClient,
) {
    suspend fun pay(request: PaymentRequest): PaymentResult {
        return client.post() {
            header("response-type", "url")
            setBody(request)
        }.body()
    }
}