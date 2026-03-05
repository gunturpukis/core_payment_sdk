package com.sdk.payment.foundation.network

import com.sdk.payment.foundation.PaymentError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

suspend inline fun <reified T> safeCall(
    block: suspend () -> HttpResponse
): Result<T> {
    return try {
        val response = block()

        when (response.status.value) {
            in 200..299 -> Result.success(response.body())
            401 -> Result.failure(PaymentError.Unauthorized)
            403 -> Result.failure(PaymentError.Forbidden)
            404 -> Result.failure(PaymentError.NotFound)
            else -> Result.failure(
                PaymentError.Server(
                    response.status.value,
                    response.bodyAsText()
                )
            )
        }
    } catch (t: Throwable) {
        Result.failure(PaymentError.Unknown(t))
    }
}