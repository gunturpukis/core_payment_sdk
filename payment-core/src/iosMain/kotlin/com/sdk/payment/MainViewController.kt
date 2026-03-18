package com.sdk.payment

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import com.sdk.payment.ui.PaymentScreen

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        PaymentScreen()
    }
}