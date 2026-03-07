package com.sdk.payment.foundation

sealed class PaymentError : Throwable() {
    object Unauthorized : PaymentError()
    object Forbidden : PaymentError()
    object NotFound : PaymentError()
    data class Server(
        val code: Int,
        override val message: String
    ) : PaymentError()
    data class Unknown(
        override val cause: Throwable
    ) : PaymentError()
}