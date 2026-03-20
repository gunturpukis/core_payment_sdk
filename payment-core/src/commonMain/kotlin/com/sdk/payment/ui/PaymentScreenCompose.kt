package com.sdk.payment.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sdk.payment.ui.component.AlertSecurity
import com.sdk.payment.ui.component.BottomSheetNfcScan
import com.sdk.payment.ui.component.CardHolderInput
import com.sdk.payment.ui.component.CardLogos
import com.sdk.payment.ui.component.CardNumberInput
import com.sdk.payment.ui.component.CardPreview
import com.sdk.payment.ui.component.ExpiryCvvRow
import com.sdk.payment.ui.component.NfcScanBottomSheet
import com.sdk.payment.ui.component.PayButton
import com.sdk.payment.ui.component.TapNfcInfo
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = remember { PaymentViewModel() }
) {
    val state = viewModel.state
    Box {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            CardPreview(state, viewModel)
            Spacer(Modifier.height(16.dp))
            TapNfcInfo {
                viewModel.showNfcSheet(true)
            }
            Spacer(Modifier.height(16.dp))
            CardNumberInput(state, viewModel)
            Spacer(Modifier.height(12.dp))
            CardLogos(state)
            Spacer(Modifier.height(16.dp))
            ExpiryCvvRow(state, viewModel)
            Spacer(Modifier.height(16.dp))
            CardHolderInput(state, viewModel)
            Spacer(Modifier.height(20.dp))
            AlertSecurity()
            Spacer(Modifier.height(20.dp))
            PayButton()
        }
        if (state.showNfcSheet) {
            BottomSheetNfcScan {
                viewModel.showNfcSheet(false)
            }
        }
    }
}
