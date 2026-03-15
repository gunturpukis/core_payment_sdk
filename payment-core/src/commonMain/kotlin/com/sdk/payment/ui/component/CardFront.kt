package com.sdk.payment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdk.payment.ui.model.CardState

@Composable
fun CardFront(state: CardState) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                .background(Color(0xFF1A73E8))
                .padding(24.dp)
        ) {
            Text(
                state.cardNumber.ifEmpty { "XXXX XXXX XXXX 2459" },
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                state.cardHolder.ifEmpty { "Card Holder" },
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomStart)
            )
            Text(
                state.expiry.ifEmpty { "**/**" },
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}