package com.sdk.payment.presentation

import com.sdk.payment.PaymentGateway
import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.domain.model.CardDetails
import com.sdk.payment.domain.model.PaymentDetails
import com.sdk.payment.domain.model.PaymentMethod
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.presentation.detector.CardTypeDetector
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BasePaymentViewModel(
    private val gateway: PaymentGateway,
    private val initialPaymentRequest: PaymentRequest,
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
        Napier.d("Processing payment...")
        Napier.d("Card: ${currentState.cardNumber}")
        if (!validateInput(currentState)) {
            _uiState.update {
                it.copy(errorMessage = "Invalid card data")
            }
            return
        }
        _uiState.update {
            it.copy(isLoading = true, errorMessage = null)
        }
        val parts = currentState.expiryDate.split("/")
        if (parts.size != 2) {
            _uiState.update {
                it.copy(errorMessage = "Invalid expiry date")
            }
            return
        }
        val month = parts.getOrNull(0)?.toIntOrNull() ?: 0
        if (month !in 1..12) {
            throw IllegalArgumentException("Invalid expiry month")
        }
        val formattedMonth = month.toString().padStart(2, '0')
        val request = initialPaymentRequest.copy(
            cardDetails = CardDetails(
                cardNumber = currentState.cardNumber.replace(" ", ""),
                cardHolderName = currentState.cardHolder,
                cardExpiredMonth = formattedMonth,
                cardExpiredYear = "20" + (parts.getOrNull(1) ?: ""),
                cardCvn = currentState.cvv
            )
        )
        try {
            val result = gateway.charge(request)
            Napier.d("Start payment request")
            Napier.d("Isinya apa : ${Json.encodeToString(request)}")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    paymentResult = result
                )
            }
            Napier.d("Payment result: $result")
        } catch (e: Exception) {
            Napier.e("Errornya apa: ${e.message.toString()}")
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }

    }
    private fun validateInput(state: PaymentUiState): Boolean {
        val card = state.cardNumber.replace(" ", "")
        return card.length >= 16 &&
                state.cardHolder.isNotEmpty() &&
                state.expiryDate.length >= 4 &&
                state.cvv.length >= 3
    }
}

