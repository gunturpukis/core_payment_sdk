package com.sdk.payment.data.remote

import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResponse
import com.sdk.payment.foundation.network.PaymentRequestAttribute
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*

class PaymentApi(
    private val client: HttpClient,
) {
    suspend fun processPayment(request: PaymentRequest): PaymentResponse {
        return client.post() {
            attributes.put(PaymentRequestAttribute, request)
            setBody(request)
        }.body()
    }
}