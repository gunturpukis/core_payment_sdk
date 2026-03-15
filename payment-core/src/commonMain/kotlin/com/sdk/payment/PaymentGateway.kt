package com.sdk.payment

import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.core.network.HttpClientFactory
import com.sdk.payment.data.remote.PaymentApi
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResponse
import com.sdk.payment.data.remote.PaymentRepository
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngine

class PaymentGateway(
    engine: HttpClientEngine,
    private val config: PaymentConfig
) {
    companion object {
        private const val SUCCESS_CODE = "00"
        private const val ERROR_CODE = "01"
    }
    private val client = HttpClientFactory.create(
        engine = engine,
        config = config
    )

    private val api = PaymentApi(client)
    private val repository = PaymentRepository(api)

    suspend fun charge(request: PaymentRequest): PaymentResponse {
        return try {
            val result = repository.pay(request)
            when {
                result.responseCode == SUCCESS_CODE -> {
                    result
                }
                else -> {
                    result
                }
            }
        } catch (e: Exception) {
           throw e
        }
    }
}