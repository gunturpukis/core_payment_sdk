package com.sdk.payment.ui.model

import com.sdk.payment.domain.model.CardType
import com.sdk.payment.domain.model.PaymentResponse

data class CardState(
    val cardNumber: String = "",
    val cardHolder: String = "",
    val expiry: String = "",
    val cvv: String = "",
    val cardBrand: String = "",
    val isCardFlipped: Boolean = false,
    val showNfcSheet: Boolean = false,
    val showCvvInfo: Boolean = false,
    val cardNumberFormatted: String = "",
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
