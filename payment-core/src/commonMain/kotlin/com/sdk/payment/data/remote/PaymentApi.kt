package com.sdk.payment.data.remote

import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResponse
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PaymentApi(
    private val client: HttpClient,
) {
    suspend fun processPayment(request: PaymentRequest): PaymentResponse {
        return client.post() {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}