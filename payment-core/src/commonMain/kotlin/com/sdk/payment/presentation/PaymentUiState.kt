package com.sdk.payment.presentation

// Data class ini akan diobservasi oleh Android XML dan SwiftUI
data class PaymentUiState(
    val cardNumber: String = "",
    val cardNumberFormatted: String = "",
    val cardHolder: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,

    val errorMessage: String? = null
)