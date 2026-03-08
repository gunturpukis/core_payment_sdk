package com.sdk.payment.presentation

import com.sdk.payment.domain.model.CardType
import com.sdk.payment.domain.model.PaymentResult

// Data class ini akan diobservasi oleh Android XML dan SwiftUI
data class PaymentUiState(
    val cardNumber: String = "",
    val cardNumberFormatted: String = "",
    val cardHolder: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val paymentResult: PaymentResult? = null,
    val cardType: CardType = CardType.UNKNOWN,
    val errorMessage: String? = null
)