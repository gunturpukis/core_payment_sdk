package com.sdk.payment.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sdk.payment.ui.model.CardState
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun CardPreview(
    state: CardState,
    vm: PaymentViewModel
) {
    val rotation by animateFloatAsState(
        if (state.isCardFlipped) 180f else 0f
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable {
                vm.flipCard(!state.isCardFlipped)
            }
    ) {
        if (rotation <= 90f) {
            CardFront(state)
        } else {
            Box(
                Modifier.graphicsLayer { rotationY = 180f }
            ) {
                CardBack(state)
            }
        }
    }
}