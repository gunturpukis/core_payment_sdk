package com.sdk.payment.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun ExpiryCvvRow(
    state: CardState,
    vm: PaymentViewModel
) {
    Row {
        OutlinedTextField(
            value = state.expiry,
            onValueChange = vm::updateExpiry,
            label = { Text("Expiry") },
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(12.dp))
        OutlinedTextField(
            value = state.cvv,
            onValueChange = vm::updateCvv,
            label = { Text("CVV") },
            modifier = Modifier.weight(1f)
        )
    }
}