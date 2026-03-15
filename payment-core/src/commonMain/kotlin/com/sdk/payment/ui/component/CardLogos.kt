package com.sdk.payment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdk.payment.ui.model.CardState

@Composable
fun CardLogos(state: CardState) {
    Row{
        CardLogo("VISA")
        CardLogo("MC")
        CardLogo("AMEX")
        CardLogo("JCB")
        CardLogo("UNION")
    }
}

@Composable
fun CardLogo(name: String) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(name, fontSize = 10.sp)
    }
    Spacer(Modifier.width(8.dp))
}