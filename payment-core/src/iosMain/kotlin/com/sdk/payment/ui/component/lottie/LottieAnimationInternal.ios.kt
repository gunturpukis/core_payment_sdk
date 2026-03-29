package com.sdk.payment.ui.component.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
//import cocoapods.lottie_ios.*

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
//            val view = LottieAnimationView()
//
//            val animation = LottieAnimation.fromJsonString(json)
//            view.animation = animation
//
//            view.loopMode = if (iterations == Int.MAX_VALUE) {
//                LottieLoopMode.Loop
//            } else {
//                LottieLoopMode.PlayOnce
//            }
//
//            if (isPlaying) {
//                view.play()
//            }
//
//            view
//        },
//        update = { view ->
//            if (isPlaying) {
//                view.play()
//            } else {
//                view.stop()
//            }
//        }
//    )
}