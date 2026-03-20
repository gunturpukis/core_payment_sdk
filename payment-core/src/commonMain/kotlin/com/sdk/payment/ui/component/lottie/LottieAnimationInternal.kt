package com.sdk.payment.ui.component.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun LottieAnimationInternal(
    json: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    iterations: Int = Int.MAX_VALUE
)