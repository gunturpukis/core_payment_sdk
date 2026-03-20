package com.sdk.payment.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sdk.payment.ui.model.CardState

class PaymentViewModel {
    var state by mutableStateOf(CardState())
        private set
    fun updateCardNumber(number: String) {
        state = state.copy(cardNumber = number)
    }
    fun updateHolder(name: String) {
        state = state.copy(cardHolder = name)
    }
    fun updateExpiry(value: String) {
        state = state.copy(expiry = value)
    }
    fun updateCvv(value: String) {
        state = state.copy(cvv = value, isCardFlipped = true)
    }
    fun flipCard(showBack: Boolean) {
        state = state.copy(isCardFlipped = showBack)
    }
    fun showNfcSheet(show: Boolean) {
        state = state.copy(showNfcSheet = show)
    }
    fun showCvvInfo(show: Boolean) {
        state = state.copy(showCvvInfo = show)
    }
    fun autofillFromNfc(
        number: String,
        expiry: String
    ) {
        state = state.copy(
            cardNumber = number,
            expiry = expiry
        )
    }
}