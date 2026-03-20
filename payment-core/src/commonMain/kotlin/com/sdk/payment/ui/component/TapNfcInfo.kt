package com.sdk.payment.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sdk.payment.Res
import com.sdk.payment.ic_tapcard
import org.jetbrains.compose.resources.painterResource

@Composable
fun TapNfcInfo(
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        modifier = Modifier.fillMaxSize().clickable { onClick() }
    ) {
        Row (
            Modifier.padding(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    resource = Res.drawable.ic_tapcard,
                ),
                contentDescription = null,
                modifier = Modifier.size(width = 40.dp, height = 40.dp),
                contentScale = ContentScale.Crop,
            )
            Spacer(Modifier.width(15.dp))
            Text(
                fontSize = 11.sp,
                 text = "Just tap the card to your device, and the card information will be filled in automatically."
            )
        }
    }
}