package com.sdk.payment.presentation

import com.sdk.payment.PaymentGateway
import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.domain.model.CardDetails
import com.sdk.payment.domain.model.PaymentDetails
import com.sdk.payment.domain.model.PaymentMethod
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.presentation.detector.CardTypeDetector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class BasePaymentViewModel(
    private val gateway: PaymentGateway,
    private val initialPaymentRequest: PaymentRequest,
//    private val initialPaymentConfig: PaymentConfig
) {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()
    fun onCardNumberChange(number: String) {
        val formatted = number
            .replace(" ", "")
            .chunked(4)
            .joinToString(" ")

        val clean = number.replace(" ", "")
        val type = CardTypeDetector.detect(clean)
        _uiState.update {
            it.copy(
                cardNumber = number,
                cardNumberFormatted = formatted,
                cardType = type
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

        val request = initialPaymentRequest.copy(
            cardDetails = CardDetails(
                cardNumber = _uiState.value.cardNumber.replace(" ", ""),
                cardHolderName = _uiState.value.cardHolder,
                cardExpiredMonth = _uiState.value.expiryDate.take(2),
                cardExpiredYear = "20" + _uiState.value.expiryDate.takeLast(2),
                cardCvn = _uiState.value.cvv
            )
        )
        try {
            val result = gateway.charge(request)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    paymentResult = result
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.message
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