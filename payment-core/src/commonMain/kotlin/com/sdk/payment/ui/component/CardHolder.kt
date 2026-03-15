package com.sdk.payment.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun CardHolderInput(
    state: CardState,
    vm: PaymentViewModel
) {
    OutlinedTextField(
        value = state.cardHolder,
        onValueChange = vm::updateHolder,
        label = { Text("Card Holder Name") },
        modifier = Modifier.fillMaxWidth()
    )
}