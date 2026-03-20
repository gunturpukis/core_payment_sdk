package com.sdk.payment.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sdk.payment.Res
import com.sdk.payment.ic_amex
import com.sdk.payment.ic_jcb
import com.sdk.payment.ic_mastercard
import com.sdk.payment.ic_unionpay
import com.sdk.payment.ic_visa
import com.sdk.payment.ui.model.CardState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun CardLogos(state: CardState) {
    Row {
        CardLogo(Res.drawable.ic_visa, "Visa")
        CardLogo(Res.drawable.ic_mastercard, "Mastercard")
        CardLogo(Res.drawable.ic_amex, "Amex")
        CardLogo(Res.drawable.ic_jcb, "Jcb")
        CardLogo(Res.drawable.ic_unionpay, "Unionpay")
    }
}

@Composable
fun CardLogo(resourceId: DrawableResource, contentDescription: String) {
    Box(
        modifier = Modifier
            .size(width = 35.dp, height = 35.dp)
            .background(Color.Transparent, shape = RoundedCornerShape(4.dp)), // Opsional: tambah rounded corner
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                resource = resourceId,
            ),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize()
                .padding(1.dp)
        )
    }
    Spacer(Modifier.width(2.dp))
}