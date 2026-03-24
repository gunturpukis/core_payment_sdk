package com.sdk.payment.presentation

import com.sdk.payment.domain.model.CardType
import com.sdk.payment.domain.model.PaymentResponse

data class PaymentUiState(
    val cardNumber: String = "",
    val cardNumberFormatted: String = "",
    val cardHolder: String = "",
    val expiryDate: String = "",
    val cvv: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val paymentResponse: PaymentResponse? = null,
    val cardType: CardType = CardType.UNKNOWN,
    val errorMessage: String? = null,

    val cardNumberError: String? = null,
    val cardHolderError: String? = null,
    val expiryDateError: String? = null,
    val cvvError: String? = null,

    var showSuccessDialog: Boolean = false,
    var showErrorDialog: Boolean = false

)