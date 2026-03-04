package com.sdk.payment.data.repository

import com.sdk.payment.data.remote.PaymentApi
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.PaymentResult
import com.sdk.payment.domain.repository.PaymentRepository
import com.sdk.payment.foundation.network.safeCall

class PaymentRepositoryImpl(
    private val api: PaymentApi
) : PaymentRepository {

    override suspend fun pay(
        request: PaymentRequest
    ): Result<PaymentResult> =
        safeCall {
            api.pay(request)
        }
}