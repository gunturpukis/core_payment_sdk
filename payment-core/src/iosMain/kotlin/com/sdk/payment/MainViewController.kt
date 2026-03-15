package com.sdk.payment

import androidx.compose.ui.window.ComposeUIViewController
import com.sdk.payment.ui.PaymentScreen
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController =
    ComposeUIViewController {
        PaymentScreen()
    }