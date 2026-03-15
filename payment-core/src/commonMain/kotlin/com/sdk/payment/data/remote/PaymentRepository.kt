package com.sdk.payment.data.remote

import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResponse

class PaymentRepository(
    private val api: PaymentApi
) {
    suspend fun pay(request: PaymentRequest): PaymentResponse {
        return try {
            api.processPayment(request)
        } catch (e: Exception) {
            throw e
        }
    }
}