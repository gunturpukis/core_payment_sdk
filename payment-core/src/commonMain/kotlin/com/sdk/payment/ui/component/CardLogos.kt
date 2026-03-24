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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sdk.payment.Res
import com.sdk.payment.domain.model.CardType
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
    val logos = listOf(
        Res.drawable.ic_visa to CardType.VISA,
        Res.drawable.ic_mastercard to CardType.MASTERCARD,
        Res.drawable.ic_amex to CardType.AMEX,
        Res.drawable.ic_jcb to CardType.JCB,
        Res.drawable.ic_unionpay to CardType.UNIONPAY
    )
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        logos.forEach { (resId, type) ->
            val isActive = state.cardType == type
            CardLogo(
                resourceId = resId,
                contentDescription = type.name,
                isActive = isActive
            )
        }
    }
}

@Composable
fun CardLogo(
    resourceId: DrawableResource,
    contentDescription: String,
    isActive: Boolean
) {
    Box(
        modifier = Modifier
            .size(width = 25.dp, height = 25.dp)
            .background(Color.Transparent, shape = RoundedCornerShape(4.dp))
            .alpha(if (isActive) 1f else 0.3f),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(resource = resourceId),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
        )
    }
    Spacer(Modifier.width(4.dp))
}