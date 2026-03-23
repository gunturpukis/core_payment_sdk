package com.sdk.payment.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.sdk.payment.PaymentGateway
import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.domain.model.CardDetails
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.getHttpEngine
import com.sdk.payment.presentation.PaymentUiState
import com.sdk.payment.presentation.detector.CardTypeDetector
import com.sdk.payment.ui.model.CardState
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class PaymentViewModel(
    token: String,
    private val jsonData: String
) : ViewModel() {
    init {
        println("Token: $token")
        println("Json: $jsonData")
    }
    val credToken = token
    val decodedString = try {
        credToken.decodeBase64String()
    }catch (e: Exception){
        println("DECODE ERROR: ${e.message}")
        ""
    }
    val parts = decodedString.split(":")
    val merchantId = parts.getOrNull(0) ?: ""
    val secretUnbound = parts.getOrNull(1) ?: ""
    val hashKey = parts.getOrNull(2) ?: ""
    private fun getPaymentRequest(): PaymentRequest {
        val json = jsonData
        return Json.decodeFromString(json)
    }
    val initialPaymentRequest = getPaymentRequest()
    val  gateway = PaymentGateway(
        engine = getHttpEngine(),
        config = PaymentConfig(
            baseUrl = "https://api-stage.mcpayment.id/card-v2/v1/charge",
            merchantId = merchantId,
            timeoutMillis = 30_000,
            secretUnbound = secretUnbound,
            hashKey = hashKey,
            apiVersion = "v3",
        ),
    )

    var state by mutableStateOf(CardState())
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun updateCardNumber(number: String) {
        val formatted = formatCardNumber(number)
        val clean = number.replace(" ", "")
        val type = CardTypeDetector.detect(clean)
        println("Detected Type: $type")
        state = state.copy(
            cardNumber = number,
            cardNumberFormatted = formatted,
            cardType = type,
            isCardFlipped = false,
            cardNumberError = null
        )
    }
    private fun formatCardNumber(number: String): String {
        return number
            .replace(" ", "")
            .chunked(4)
            .joinToString(" ")
    }
    fun updateHolder(name: String) {
        val uppercase = name.uppercase()
        state = state.copy(
            cardHolder = uppercase,
            isCardFlipped = false,
            cardHolderError = null
        )
    }
    fun onExpiryDateChange(value: String) {
        val formatted = updateExpiry(value)
           state = state.copy(
                expiry = formatted,
                isCardFlipped = false,
                expiryDateError = null
            )

    }
    fun updateExpiry(value: String): String {
        val clean = value.replace("[^\\d]".toRegex(), "")
        return when {
            clean.length >= 2 -> {
                val month = clean.substring(0, 2)
                val year = clean.substring(2).take(2)
                "$month/$year"
            }
            else -> clean
        }
    }
    fun updateCvv(value: String) {
        state = state.copy(
            cvv = value,
            isCardFlipped = true,
            cvvError = null
        )
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
    private fun validateInput(state: CardState): CardState {
        val cardNumberError = validateCardNumber(state.cardNumber)
        val cardHolderError = validateCardHolder(state.cardHolder)
        val expiryDateError = validateExpiryDate(state.expiry)
        val cvvError = validateCvv(state.cvv)

        return state.copy(
            cardNumberError = cardNumberError,
            expiryDateError = expiryDateError,
            cvvError = cvvError,
            cardHolderError = cardHolderError
        )
    }
    private fun isAllValid(state: CardState): Boolean {
        return state.cardNumberError == null &&
                state.expiryDateError == null &&
                state.cvvError == null &&
                state.cardHolderError == null
    }

    fun processPayment(coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        try {
            val validatedState = validateInput(state)
            if (!isAllValid(state)) {
                state = state.copy(errorMessage = "Please fix the errors above")
                return@launch
            }
            state = state.copy(isLoading = true, errorMessage = null)
            val expiryParts = state.expiry.split("/")
            val month = expiryParts.getOrNull(0)?.padStart(2, '0') ?: "00"
            val year = "20" + (expiryParts.getOrNull(1) ?: "00")
            val paymentRequest = buildPaymentRequest(validatedState, month, year)

            val result = gateway.charge(paymentRequest)
            state = state.copy(isLoading = false, paymentResponse = result)
        } catch (e: Exception) {
            state = state.copy(isLoading = false, errorMessage = e.message)
        }
    }
}

    fun clearErrors() {
        state = state.copy(
            cardNumberError = null,
            expiryDateError = null,
            cvvError = null,
            cardHolderError = null,
            errorMessage = null
        )
    }
    private fun buildPaymentRequest(
        state: CardState,
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
    fun reset() {
        state = CardState()
        _isLoading.value = false
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
//    private fun updateValidationErrors(state: PaymentUiState) {
//        updateFieldError(
//            binding.tilCardNumber,
//            binding.lblCardNumber,
//            state.cardNumberError
//        )
//        updateFieldError(
//            binding.tilCardName2,
//            binding.lblCardNumber2,
//            state.cardHolderError
//        )
//        updateFieldError(
//            binding.tilExpDate,
//            binding.lblExpDate,
//            state.expiryDateError
//        )
//        updateFieldErrorWithIcon(
//            binding.tilCvv,
//            binding.lblCvv,
//            binding.btnInfoCvv,
//            state.cvvError
//        )
//    }
//    private fun updateFieldError(
//        textInputLayout: TextInputLayout,
//        label: TextView,
//        error: String?
//    ) {
//        textInputLayout.error = error
//        label.setTextColor(if (error != null) Color.RED else Color.DKGRAY)
//    }
//    private fun updateFieldErrorWithIcon(
//        textInputLayout: TextInputLayout,
//        label: TextView,
//        iconButton: ImageView,
//        error: String?
//    ) {
//        updateFieldError(textInputLayout, label, error)
//        iconButton.setColorFilter(if (error != null) Color.RED else Color.DKGRAY)
//    }
}