package com.sdk.payment.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun ExpiryCvvRow(
    state: CardState,
    vm: PaymentViewModel
) {
    val isExpiryError = state.expiryDateError != null
    val isCvvError = state.cvvError != null
    Row {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                fontSize = 12.sp,
                text = "Expired Date",
                fontWeight = FontWeight.Normal,
                color = if (isExpiryError) Color.Red else Color.DarkGray
            )
            OutlinedTextField(
                value = state.expiry,
                onValueChange = vm::onExpiryDateChange,
                modifier = Modifier.fillMaxWidth(),
                isError = isExpiryError
            )
            if (isExpiryError) {
                Spacer(Modifier.height(4.dp))
                Text(
                    fontSize = 10.sp,
                    text = state.expiryDateError ?: "",
                    color = Color.Red
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row {
                Text(
                    fontSize = 12.sp,
                    text = "CVN/CVV",
                    fontWeight = FontWeight.Normal,
                    color = if (isCvvError) Color.Red else Color.DarkGray
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = if (isCvvError) Color.Red else Color(0xFF504F4F),
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { vm.showCvvInfo(true) }
                )
            }
            OutlinedTextField(
                value = state.cvv,
                onValueChange = vm::updateCvv,
                modifier = Modifier.fillMaxWidth(),
                isError = isCvvError
            )
            if (isCvvError) {
                Spacer(Modifier.height(4.dp))
                Text(
                    fontSize = 10.sp,
                    text = state.cvvError ?: "",
                    color = Color.Red
                )
            }
        }
    }
}