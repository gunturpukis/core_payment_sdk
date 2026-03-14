package com.sdk.payment.presentation

import com.sdk.payment.PaymentGateway
import com.sdk.payment.domain.model.CardDetails
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.presentation.detector.CardTypeDetector
//import io.github.aakira.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BasePaymentViewModel(
    private val gateway: PaymentGateway,
    private val initialPaymentRequest: PaymentRequest,
) {
    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onCardNumberChange(number: String) {
        val formatted = formatCardNumber(number)
        val clean = number.replace(" ", "")
        val type = CardTypeDetector.detect(clean)
        _uiState.update {
            it.copy(
                cardNumber = clean,
                cardNumberFormatted = formatted,
                cardType = type,
                cardNumberError = null
            )
        }
    }
    fun onCardHolderChange(name: String) {
        val uppercase = name.uppercase()
        _uiState.update {
            it.copy(
                cardHolder = uppercase,
                cardHolderError = null
            )
        }
    }
    fun onExpiryDateChange(date: String) {
        val formatted = formatExpiryDate(date)
        _uiState.update {
            it.copy(
                expiryDate = formatted,
                expiryDateError = null
            )
        }
    }
    fun onCvvChange(cvv: String) {
        _uiState.update {
            it.copy(
                cvv = cvv,
                cvvError = null
            )
        }
    }
    private fun formatCardNumber(number: String): String {
        return number
            .replace(" ", "")
            .chunked(4)
            .joinToString(" ")
    }
    private fun formatExpiryDate(date: String): String {
        val clean = date.replace("[^\\d]".toRegex(), "")
        return when {
            clean.length >= 2 -> {
                val month = clean.substring(0, 2)
                val year = clean.substring(2).take(2)
                "$month/$year"
            }
            else -> clean
        }
    }
    private fun validateCardNumber(cardNumber: String): String? {
        val clean = cardNumber.replace(" ", "")
        return when {
            clean.isEmpty() -> "Card number cannot be empty"
            clean.length < 13 -> "Card number must be at least 13 digits"
            clean.length > 19 -> "Card number cannot exceed 19 digits"
            !clean.all { it.isDigit() } -> "Card number must contain only digits"
            else -> null
        }
    }
    private fun validateCardHolder(cardHolder: String): String? {
        return when {
            cardHolder.isEmpty() -> "Card holder name cannot be empty"
            cardHolder.length < 3 -> "Card holder name must be at least 3 characters"
            cardHolder.any { it.isDigit() } -> "Card holder name cannot contain numbers"
            else -> null
        }
    }
    private fun validateExpiryDate(expiryDate: String): String? {
        val parts = expiryDate.split("/")
        return when {
            expiryDate.isEmpty() -> "Expiry date cannot be empty"
            parts.size != 2 -> "Expiry date must be in MM/YY format"
            else -> {
                val month = parts.getOrNull(0)?.toIntOrNull() ?: 0
                when {
                    month !in 1..12 -> "Month must be between 01 and 12"
                    else -> null
                }
            }
        }
    }
    private fun validateCvv(cvv: String): String? {
        return when {
            cvv.isEmpty() -> "CVV cannot be empty"
            cvv.length < 3 -> "CVV must be at least 3 digits"
            cvv.length > 4 -> "CVV cannot exceed 4 digits"
            !cvv.all { it.isDigit() } -> "CVV must contain only digits"
            else -> null
        }
    }
    private fun validateInput(state: PaymentUiState): PaymentUiState {
        val cardNumberError = validateCardNumber(state.cardNumber)
        val cardHolderError = validateCardHolder(state.cardHolder)
        val expiryDateError = validateExpiryDate(state.expiryDate)
        val cvvError = validateCvv(state.cvv)

        return state.copy(
            cardNumberError = cardNumberError,
            expiryDateError = expiryDateError,
            cvvError = cvvError,
            cardHolderError = cardHolderError
        )
    }
    private fun isAllValid(state: PaymentUiState): Boolean {
        return state.cardNumberError == null &&
                state.expiryDateError == null &&
                state.cvvError == null &&
                state.cardHolderError == null
    }
    fun processPayment(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            try {
                //Napier.d("Starting payment process...")
                val currentState = _uiState.value
                val validatedState = validateInput(currentState)
                _uiState.value = validatedState

                if (!isAllValid(validatedState)) {
                    //Napier.d("Validation failed")
                    _uiState.update {
                        it.copy(
                            errorMessage = "Please fix the errors above",
                            isLoading = false
                        )
                    }
                    return@launch
                }
                _isLoading.value = true
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                val expiryParts = validatedState.expiryDate.split("/")
                val month = expiryParts.getOrNull(0)?.padStart(2, '0') ?: "00"
                val year = "20" + (expiryParts.getOrNull(1) ?: "00")
                val paymentRequest = buildPaymentRequest(validatedState, month, year)

                //Napier.d("Payment Request: ${Json.encodeToString(paymentRequest)}")
                val result = gateway.charge(paymentRequest)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        paymentResponse = result,
                        errorMessage = null
                    )
                }
                //Napier.d("Payment successful: ${result.data?.link}")
            } catch (e: Exception) {
                //Napier.e("Payment failed: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Payment failed",
                        isLoading = false
                    )
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun buildPaymentRequest(
        state: PaymentUiState,
        month: String,
        year: String
    ): PaymentRequest {
        return initialPaymentRequest.copy(
            cardDetails = CardDetails(
                cardNumber = state.cardNumber,
                cardHolderName = state.cardHolder,
                cardExpiredMonth = month,
                cardExpiredYear = year,
                cardCvn = state.cvv
            )
        )
    }
    fun clearErrors() {
        _uiState.update {
            it.copy(
                cardNumberError = null,
                expiryDateError = null,
                cvvError = null,
                cardHolderError = null,
                errorMessage = null
            )
        }
    }
    fun reset() {
        _uiState.value = PaymentUiState()
        _isLoading.value = false
    }
}
