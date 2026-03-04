package com.sdk.payment.domain.repository

import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResult

interface PaymentRepository {
    suspend fun pay(request: PaymentRequest): Result<PaymentResult>
}