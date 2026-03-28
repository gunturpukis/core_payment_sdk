package com.sdk.payment.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sdk.payment.nfc.NfcManager
import com.sdk.payment.nfc.provideNfcManager
import com.sdk.payment.ui.component.AlertSecurity
import com.sdk.payment.ui.component.BottomSheetNfcScan
import com.sdk.payment.ui.component.CardHolderInput
import com.sdk.payment.ui.component.CardLogos
import com.sdk.payment.ui.component.CardNumberInput
import com.sdk.payment.ui.component.CardPreview
import com.sdk.payment.ui.component.CvvInfoDialog
import com.sdk.payment.ui.component.ExpiryCvvRow
import com.sdk.payment.ui.component.PayButton
import com.sdk.payment.ui.component.PaymentFailedDialog
import com.sdk.payment.ui.component.PaymentLoadingDialog
import com.sdk.payment.ui.component.PaymentSuccessDialog
import com.sdk.payment.ui.component.TapNfcInfo
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()
    Box {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            CardPreview(state, viewModel)
            Spacer(Modifier.height(10.dp))
            TapNfcInfo {
                viewModel.showNfcSheet(true)
                viewModel.startNfcScan()
            }
            Spacer(Modifier.height(10.dp))
            CardNumberInput(state, viewModel)
            Spacer(Modifier.height(10.dp))
            CardLogos(state)
            Spacer(Modifier.height(5.dp))
            ExpiryCvvRow( state, viewModel,)
            Spacer(Modifier.height(10.dp))
            CardHolderInput(state, viewModel)
            Spacer(Modifier.height(15.dp))
            AlertSecurity()
            Spacer(Modifier.height(10.dp))
            PayButton(
                isLoading = state.isLoading,
                onClick = {viewModel.onPayClicked (scope)}
            )
        }
        if (state.showNfcSheet) {
            BottomSheetNfcScan {
                viewModel.showNfcSheet(false)
            }
        }
        if (state.showCvvInfo) {
            CvvInfoDialog {
                viewModel.showCvvInfo(false)
            }
        }
        PaymentLoadingDialog(isVisible = state.isLoading)
        PaymentSuccessDialog(
            isVisible = state.showSuccessDialog,
            onDone = {
                viewModel.onSuccessDone()
            }
        )
        if(state.showErrorDialog){
            PaymentFailedDialog(
                isVisible = state.showErrorDialog,
                onDone = {
                    viewModel.onFailedDone()
                }
            )
        }
    }
}
