package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class PaymentMethod {

    @Serializable
    data class CreditCard(
        val cardNumber: String,
        val expiryMonth: Int,
        val expiryYear: Int,
        val cvv: String
    ) : PaymentMethod()

    @Serializable
    data class EWallet(
        val provider: String,
        val phoneNumber: String
    ) : PaymentMethod()

    @Serializable
    data class BankTransfer(
        val bankCode: String
    ) : PaymentMethod()
}