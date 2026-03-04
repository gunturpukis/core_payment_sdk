package com.sdk.payment

import com.sdk.payment.data.remote.PaymentApi
import com.sdk.payment.data.repository.PaymentRepositoryImpl
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResult
import io.ktor.client.*

class PaymentGateway(
    client: HttpClient,
    baseUrl: String
) {
    private val api = PaymentApi(client, baseUrl)
    private val repository = PaymentRepositoryImpl(api)

    suspend fun pay(
        request: PaymentRequest
    ): Result<PaymentResult> =
        repository.pay(request)
}