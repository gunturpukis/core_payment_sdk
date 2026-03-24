package com.sdk.payment.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun CardHolderInput(
    state: CardState,
    vm: PaymentViewModel
) {
    val isError = state.cardHolderError != null
    Column {
        Text(
            fontSize = 12.sp,
            text = "Card Holder Name",
            fontWeight = FontWeight.Normal,
            color = if (isError) Color.Red else Color.DarkGray
        )
        Spacer(Modifier.height(5.dp))
        OutlinedTextField(
            value = state.cardHolder,
            onValueChange = vm::updateHolder,
            modifier = Modifier.fillMaxWidth(),
            isError = isError
        )
        if (isError) {
            Spacer(Modifier.height(4.dp))
            Text(
                fontSize = 10.sp,
                text = state.cardHolderError ?: "",
                color = Color.Red
            )
        }
    }
}