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
import com.sdk.payment.ui.route.PaymentSDKNavHost
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import io.ktor.util.*
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding

fun MainViewController(): UIViewController = ComposeUIViewController {
    var showSDK by remember { mutableStateOf(false) }

    val paymentRequest = remember {
        PaymentRequest(
            externalId = "14rRAb1eiS",
            orderId = "OKo2H5B8gm",
            currency = "IDR",
            paymentMethod = "card",
            paymentChannel = "BRICC",
            paymentMode = "CLOSE",
            paymentDetails = PaymentDetails(
                amount = 10000,
                transactionDescription = "Clothes",
                isCustomerPayingFee = false,
                expiredTime = ""
            ),
            customerDetails = CustomerDetails(
                email = "solutions@ifortepay.id",
                fullName = "Testing",
                phone = "08970799128",
                ipAddress = "182.30.91.67"
            ),
            cardDetails = CardDetails(
                cardNumber = "",
                cardExpiredMonth = "",
                cardExpiredYear = "",
                cardCvn = "",
                cardHolderName = ""
            ),
            returnUrl = "https://superapp-stg.ifortepay.id/",
            callbackUrl = "https://mcpid.free.beeceptor.com",
            itemDetails = listOf(
                ItemDetails(
                    itemId = "Artikel 1",
                    name = "shirt",
                    amount = 10000,
                    qty = 1,
                    description = "3131"
                )
            ),
            billingAddress = BillingAddres(
                fullName = "CC Test",
                phone = "0893456789",
                address = "Kosan Hj Hasan",
                city = "Tangerang",
                postalCode = "19127",
                country = "ID"
            ),
            shippingAddress = ShippingAddres(
                fullName = "MCP",
                phone = "0893456789",
                address = "Bandara Mas",
                city = "Malang",
                postalCode = "10210",
                country = "ID"
            ),
            paymentOptions = PaymentOptions(
                useRewards = true,
                campaign_code = "002",
                tenor = "0"
            ),
            additionalData = "",
            source = "payment_page",
        )
    }

    val credentialData = remember {
        val merchantId = "MC2026016183"
        val secretUnbound = "0x85e19e9ff024614509"
        val hashKey = "U07P9kpkmYeUDLiqZZVymciPnr3QdN/tL+XBL3Adkck"

        val credential = "${merchantId}:${secretUnbound}:${hashKey}"
        val token = NSString.create(string = credential)
            .dataUsingEncoding(NSUTF8StringEncoding)
            ?.base64EncodedStringWithOptions(0u)
//        val token = credential.encodeBase64()
//        val json = Json.encodeToString(paymentRequest)
        val json = Json {
            encodeDefaults = false
            explicitNulls = false
        }.encodeToString(paymentRequest)

        Pair(token, json)
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        if (showSDK) {
            MaterialTheme { // Tambahkan pembungkus Theme
                Surface(modifier = Modifier.fillMaxSize()) {
//                    PaymentSDKNavHost(
//                        startToken = credentialData.first,
//                        startData = credentialData.second,
//                    )
                    PaymentScreen(
                        token = credentialData.first.toString(),
                        jsonData = credentialData.second,
                    )

                }
            }
        } else {
            Button(onClick = { showSDK = true }) {
                Text("Bayar Sekarang (Open SDK iOS)")
            }
        }
    }
}