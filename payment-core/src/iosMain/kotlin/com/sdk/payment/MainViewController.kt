package com.sdk.payment

import com.sdk.payment.ui.PaymentScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.sdk.payment.nfc.NfcManager
import com.sdk.payment.ui.viewmodel.PaymentViewModel
import platform.UIKit.UIViewController
import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.util.*

fun MainViewController(
    token: String,
    json: String,
    onResult : (Boolean) -> Unit
): UIViewController = ComposeUIViewController {
    MaterialTheme {
        Surface {
            val viewModel = remember {
                PaymentViewModel(
                    token = token, jsonData = json, onResultCallback = onResult,
                    nfcManager = NfcManager()
                )
            }
            PaymentScreen(
                viewModel = viewModel
            )
        }
    }
}