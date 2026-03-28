package com.sdk.payment

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import com.sdk.payment.ui.PaymentScreen

//fun MainViewController(): UIViewController {
//    return ComposeUIViewController {
////        PaymentScreen()
//    }
//}
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sdk.payment.domain.model.BillingAddres
import com.sdk.payment.domain.model.CardDetails
import com.sdk.payment.domain.model.CustomerDetails
import com.sdk.payment.domain.model.ItemDetails
import com.sdk.payment.domain.model.PaymentDetails
import com.sdk.payment.domain.model.PaymentOptions
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.ShippingAddres
import com.sdk.payment.nfc.NfcManager
import com.sdk.payment.ui.route.PaymentSDKNavHost
import com.sdk.payment.ui.viewmodel.PaymentViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import io.ktor.util.*
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding

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