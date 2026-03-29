package com.sdk.payment.ui.component.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
actual fun LottieAnimationInternal(
    json: String,
    modifier: Modifier,
    isPlaying: Boolean,
    iterations: Int
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(json)
    )
    LottieAnimation(
        composition = composition,
        iterations = iterations,
        isPlaying = isPlaying,
        modifier = modifier
    )
}