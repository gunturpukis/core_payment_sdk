package com.sdk.payment.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun CardNumberInput(
    state: CardState,
    vm: PaymentViewModel
) {
    Column(
        modifier = Modifier.clickable {
            vm.flipCard(state.isCardFlipped)
        }
    ) {
        Text(
            "Card Number",
            fontWeight = FontWeight.Normal
        )
        Spacer(Modifier.height(5.dp))
        OutlinedTextField(
            value = state.cardNumber,
            onValueChange = vm::updateCardNumber,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }

}