package com.sdk.payment

import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.core.network.HttpClientFactory
import com.sdk.payment.data.remote.PaymentApi
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResult
import com.sdk.payment.data.remote.PaymentRepository
import io.ktor.client.*
import io.ktor.client.engine.HttpClientEngine

class PaymentGateway(
    engine: HttpClientEngine,
    private val config: PaymentConfig
) {
    private val client = HttpClientFactory.create(
        engine = engine,
        config = config
    )
    private val api = PaymentApi(client)
    private val repository = PaymentRepository(api)
    suspend fun charge(request: PaymentRequest): PaymentResult {
        return try {
            repository.pay(request)
        } catch (e: Exception) {
            PaymentResult(e.message ?: "Unknown error")
        }
    }
}