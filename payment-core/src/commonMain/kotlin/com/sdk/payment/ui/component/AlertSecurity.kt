package com.sdk.payment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AlertSecurity() {
    Card(
        colors = CardDefaults.cardColors(contentColor = Color(0xFF101010))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFEDFAFF))

        ){
            Column(
                Modifier.padding(20.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = "Your card details are securely protected.",
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    textAlign = TextAlign.Start,
                    text = "We collaborate with banks to ensure the security of your card data. We will not access or misuse your card information."
                )
            }
        }

    }
}