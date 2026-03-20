package com.sdk.payment.ui.component.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView

@Composable
actual fun LottieAnimationInternal(
    json: String,
    modifier: Modifier,
    isPlaying: Boolean,
    iterations: Int
) {
//    UIKitView(
//        modifier = modifier,
//        factory = {
//            val animationView = LottieAnimationView()
//
//            val animation = LottieAnimation.fromJson(json, null)
//            animationView.animation = animation
//
//            animationView.loopMode =
//                if (iterations == Int.MAX_VALUE) LottieLoopMode.loop
//                else LottieLoopMode.playOnce
//
//            if (isPlaying) {
//                animationView.play()
//            }
//
//            animationView
//        },
//        update = { view ->
//            if (isPlaying) view.play() else view.pause()
//        }
//    )
}