package com.sdk.payment.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun CardNumberInput(
    state: CardState,
    vm: PaymentViewModel
) {
    val isError = state.cardNumberError != null

    Column(
        modifier = Modifier.clickable {
            vm.flipCard(state.isCardFlipped)
        }
    ) {
        Text(
            fontSize = 12.sp,
            text = "Card Number",
            fontWeight = FontWeight.Normal,
            color = if (isError) Color.Red else Color.DarkGray
        )
        OutlinedTextField(
            value = state.cardNumber,
            shape = RoundedCornerShape(20.dp),
            onValueChange = {
                val filtered = it.filter { char -> char.isDigit() }
                    .take(16)
                vm.updateCardNumber(filtered)
            },
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = isError
        )
        if (isError) {
            Spacer(Modifier.height(4.dp))
            Text(
                fontSize = 10.sp,
                text = state.cardNumberError ?: "",
                color = Color.Red
            )
        }
    }
}