package com.sdk.payment.ui.component.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSData
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
//            val view = LottieAnimationView(frame = CGRectZero.readValue())
//
//            view.setAnimationFromJson(json, null)
//
//            view.loopMode = when {
//                iterations == Int.MAX_VALUE -> LottieLoopMode.loop
//                iterations > 1 -> LottieLoopMode.repeat
//                else -> LottieLoopMode.playOnce
//            }
//
//            if (isPlaying) view.play()
//
//            view
//        },
//        update = { view ->
//            if (isPlaying) view.play() else view.pause()
//        }
//    )
}