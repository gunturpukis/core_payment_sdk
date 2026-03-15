package com.sdk.payment.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun CardNumberInput(
    state: CardState,
    vm: PaymentViewModel
) {
    OutlinedTextField(
        value = state.cardNumber,
        onValueChange = vm::updateCardNumber,
        label = { Text("Card Number") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}