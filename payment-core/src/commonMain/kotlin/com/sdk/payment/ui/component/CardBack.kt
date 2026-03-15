package com.sdk.payment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sdk.payment.ui.model.CardState

@Composable
fun CardBack(state: CardState) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .background(Color.DarkGray)
                .padding(24.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.Black)
            )
            Spacer(Modifier.height(20.dp))
            Box(
                Modifier
                    .width(80.dp)
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(state.cvv.ifEmpty { "***" })
            }
        }
    }
}