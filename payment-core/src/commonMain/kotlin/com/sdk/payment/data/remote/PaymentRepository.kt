package com.sdk.payment.data.remote

import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResult

class PaymentRepository(
    private val api: PaymentApi
) {
    suspend fun pay(request: PaymentRequest): PaymentResult {
        return api.pay(request)
    }
}