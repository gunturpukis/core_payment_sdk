package com.sdk.payment.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdk.payment.Res
import com.sdk.payment.ic_card
import com.sdk.payment.ic_tapcard
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetNfcScan(
    onClose: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onClose
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Image(
                painter = painterResource(
                    resource = Res.drawable.ic_tapcard,
                ),
                contentDescription = null,
                modifier = Modifier.size(width = 100.dp, height = 100.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Scan your card",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))
            Text(
                modifier = Modifier.padding(horizontal = 15.dp),
                fontSize = 12.sp,
                text =   "Place your debit/credit card on the back of your phone and hold it for at least 3 seconds. Make sure your card and device support NFC features.",
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}