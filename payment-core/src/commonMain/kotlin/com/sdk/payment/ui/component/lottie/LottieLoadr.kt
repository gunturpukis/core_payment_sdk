package com.sdk.payment.ui.component.lottie


import androidx.compose.runtime.*
import com.sdk.payment.Res

@Composable
fun rememberLottieState(path: String): State<LottieState> {
    return produceState<LottieState>(initialValue = LottieState.Loading, path) {
        value = try {
            val json = Res.readBytes(path).decodeToString()
            LottieState.Success(json)
        } catch (e: Exception) {
            e.printStackTrace()
            LottieState.Error(e.message)
        }
    }
}