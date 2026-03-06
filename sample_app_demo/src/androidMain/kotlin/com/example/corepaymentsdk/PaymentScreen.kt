package com.example.corepaymentsdk

import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.sdk.payment.ui.PaymentActivity

@Composable
fun PaymentScreen() {

    val context = LocalContext.current

    Button(
        onClick = {

            val intent = Intent(context, PaymentActivity::class.java)

            context.startActivity(intent)

        }
    ) {
        Text("Open Payment SDK")
    }
}