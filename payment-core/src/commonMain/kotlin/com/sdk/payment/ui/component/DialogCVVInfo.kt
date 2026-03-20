package com.sdk.payment.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.sdk.payment.Res
import com.sdk.payment.ic_cvv
import com.sdk.payment.ic_tapcard
import org.jetbrains.compose.resources.painterResource

@Composable
fun CvvInfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(15.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Text(
                    text = "CVN/CVV Code",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                CardImagePlaceholder()
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "The CVN/CVV code consists of 3 or 4 digits as shown on your debit/credit card.",
                    fontSize = 14.sp,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun CardImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                resource = Res.drawable.ic_cvv,
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}