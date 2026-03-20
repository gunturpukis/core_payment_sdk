package com.sdk.payment.ui.component.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LottieView(
    path: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    iterations: Int = Int.MAX_VALUE,
    loading: @Composable (() -> Unit)? = null,
    error: @Composable ((String?) -> Unit)? = null
) {
    val state = rememberLottieState(path)

    when (val result = state.value) {
        is LottieState.Loading -> {
            loading?.invoke()
        }

        is LottieState.Error -> {
            error?.invoke(result.message)
        }

        is LottieState.Success -> {
            LottieAnimationInternal(
                json = result.json,
                modifier = modifier,
                isPlaying = isPlaying,
                iterations = iterations
            )
        }
    }
}