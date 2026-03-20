package com.sdk.payment.ui.component.lottie

sealed class LottieState {
    object Loading : LottieState()
    data class Success(val json: String) : LottieState()
    data class Error(val message: String? = null) : LottieState()
}