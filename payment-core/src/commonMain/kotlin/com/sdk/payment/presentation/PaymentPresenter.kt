package com.sdk.payment.presentation

import com.sdk.payment.PaymentGateway
import com.sdk.payment.domain.model.PaymentRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BasePaymentViewModel(
    private val gateway: PaymentGateway
) {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun onCardNumberChange(number: String) {
        val formatted = number
            .replace(" ", "")
            .chunked(4)
            .joinToString(" ")
        _uiState.update {
            it.copy(
                cardNumber = number,
                cardNumberFormatted = formatted
            )
        }
    }

    fun onCardHolderChange(name: String) {
        _uiState.update {
            it.copy(cardHolder = name.uppercase())
        }
    }

    fun onExpiryDateChange(date: String) {
        _uiState.update {
            it.copy(expiryDate = date)
        }
    }

    fun onCvvChange(cvv: String) {
        _uiState.update {
            it.copy(cvv = cvv)
        }
    }

    suspend fun processPayment() {
        val currentState = _uiState.value
        if (!validateInput(currentState)) {
            _uiState.update {
                it.copy(errorMessage = "Invalid card data")
            }
            return
        }
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null)
        }
        val request = PaymentRequest(
            cardNumber = currentState.cardNumber.replace(" ", ""),
            cardHolder = currentState.cardHolder,
            expiryDate = currentState.expiryDate,
            cvv = currentState.cvv
        )
        val result = gateway.pay(request)
        result.onSuccess {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isSuccess = true
                )
            }
        }.onFailure { error ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
        }
    }

    private fun validateInput(state: PaymentUiState): Boolean {
        return state.cardNumber.length >= 16 &&
                state.cardHolder.isNotEmpty() &&
                state.expiryDate.length >= 4 &&
                state.cvv.length >= 3
    }
}