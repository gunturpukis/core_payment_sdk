package com.sdk.payment.data.remote

import com.sdk.payment.domain.model.PaymentRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class PaymentApi(
    private val client: HttpClient,
    private val baseUrl: String
) {

    suspend fun pay(request: PaymentRequest): HttpResponse =
        client.post("$baseUrl/pay") {
            setBody(request)
        }
}