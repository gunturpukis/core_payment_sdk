package com.example.corepaymentsdk

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.sdk.payment.ui.PaymentActivity
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun PaymentScreenAndroid() {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val paymentRequest = PaymentRequest(
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
                expiredTime =""
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
        val merchantId = "MC2026016183"
        val secretUnbound = "0x85e19e9ff024614509"
        val hashKey = "U07P9kpkmYeUDLiqZZVymciPnr3QdN/tL+XBL3Adkck"
        val credential = "${merchantId}:${secretUnbound}:${hashKey}"
        val credentiialToken = credential.toByteArray().encodeBase64()

            Button(
                onClick = {
                    val jsonString = Json.encodeToString(paymentRequest)
                    val intent = Intent(context, PaymentActivity::class.java).apply {
                        putExtra("credentialToken", credentiialToken)
                        putExtra("Payment_Data", jsonString)
                    }
                    context.startActivity(intent)
                }
            ) {
                Text("Open Payment SDK")
            }
    }


}