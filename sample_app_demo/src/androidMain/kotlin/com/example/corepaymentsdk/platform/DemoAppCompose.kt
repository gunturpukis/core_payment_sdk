package com.example.corepaymentsdk.platform

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sdk.payment.domain.model.BillingAddres
import com.sdk.payment.domain.model.CardDetails
import com.sdk.payment.domain.model.CustomerDetails
import com.sdk.payment.domain.model.ItemDetails
import com.sdk.payment.domain.model.PaymentDetails
import com.sdk.payment.domain.model.PaymentOptions
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.ShippingAddres
import com.sdk.payment.nfc.NfcManager
import com.sdk.payment.ui.PaymentScreen
import com.sdk.payment.ui.viewmodel.PaymentViewModel
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun DemoAppScreen(
) {
    val paymentRequest = PaymentRequest(
        externalId = "1Ktru19Cp7",
        orderId = "ONxeTcEBE6",
        currency = "IDR",
        source = "payment_page",
        paymentMethod = "card",
        paymentChannel = "BRICC",
        paymentMode = "CLOSE",
        paymentDetails = PaymentDetails(
        amount = 10000,
        isCustomerPayingFee = false,
        transactionDescription = "Clothes",
        expiredTime = "",
        ),
        itemDetails = listOf(
            ItemDetails(
                itemId = "Artikel 1",
                name = "shirt",
                amount = 10000,
                qty = 1,
                description = "3131"
            ),
        ),
        customerDetails = CustomerDetails(
        email = "solutions@ifortepay.id",
        fullName = "Testing",
        phone = "08970799128",
        ipAddress = "182.30.91.67",
        ),
        billingAddress = BillingAddres(
        fullName = "CC Test",
        phone = "0893456789",
        address = "Kosan Hj Hasan",
        city = "Tangerang",
        postalCode = "19127",
        country = "ID",
        ),
        shippingAddress = ShippingAddres(
        fullName = "MCP",
        phone = "0893456789",
        address = "Bandara Mas",
        city = "Malang",
        postalCode = "10210",
        country = "ID",
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
        paymentOptions = PaymentOptions(
        useRewards = true,
        campaign_code = "002",
        tenor = "0"
        ),
        additionalData = "",
        )
    val merchantId = "MC2026016183"
    val secretUnbound = "0x85e19e9ff024614509"
    val hashKey = "U07P9kpkmYeUDLiqZZVymciPnr3QdN/tL+XBL3Adkck"
    val credential = "${merchantId}:${secretUnbound}:${hashKey}"
    val credentiialToken = credential.toByteArray().encodeBase64()
    val jsonString = Json.encodeToString(paymentRequest)

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize())
            {
                val context = LocalContext.current
                val activity = context as Activity
                val viewModel = remember {
                PaymentViewModel(
                    token = credentiialToken, jsonData = jsonString,
                    nfcManager = NfcManager(activity = activity)
                )
            }
                PaymentScreen(
                   viewModel = viewModel
                )
            }
        }
    }

}