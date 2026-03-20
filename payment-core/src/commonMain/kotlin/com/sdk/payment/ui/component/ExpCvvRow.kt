package com.sdk.payment.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun ExpiryCvvRow(
    state: CardState,
    vm: PaymentViewModel
) {
    Row {
        Column(
                modifier = Modifier.weight(1f)
        ) {
            Text(
                "Expired Date",
                fontWeight = FontWeight.Normal
            )
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = state.expiry,
                onValueChange = vm::updateExpiry,
            )
        }
            Spacer(Modifier.width(12.dp))
        Column (
                modifier = Modifier.weight(1f)
        ){
            Row {
                Text(
                    "CVN/CVV",
                    fontWeight = FontWeight.Normal
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Default.Info, null,
                    tint = Color(0xFF504F4F),
                    modifier = Modifier.size(15.dp)
                        .clickable{vm.showCvvInfo(true)},
                    )
            }
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                value = state.cvv,
                onValueChange = vm::updateCvv,
            )
        }

    }
}