package com.example.corepaymentsdk

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sdk.payment.PaymentGateway
import com.sdk.payment.domain.model.PaymentRequest
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch


@Composable
fun PaymentScreen() {

    var resultText by remember { mutableStateOf("No Transaction") }

//    val gateway = remember {
//        PaymentGateway.create(
//            baseUrl = "https://sandbox.api.com",
//            client = HttpClient()
//        )
//    }
        val gateway: PaymentGateway = remember {
            PaymentGateway(
                baseUrl = "https://sandbox.api.com",
                client = HttpClient()
            )
        }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = resultText)

        Spacer(modifier = Modifier.height(16.dp))

        val scope = rememberCoroutineScope()
        var result by remember { mutableStateOf("Idle") }

        Button(
            onClick = {
                scope.launch {
//                    resultText = "Processing..."
                     val result = gateway.pay(
                        PaymentRequest( amount = 10000, currency = "IDR")
                    )
                    result.fold(
                        onSuccess = { data ->
                            resultText = "Success: ${data.transactionId}"
                        },
                        onFailure = { error ->
                            resultText = "Error: ${error.message}"
                        }
                    )

                }
            }
        ) {
            Text("Pay Now")
        }
    }
}

