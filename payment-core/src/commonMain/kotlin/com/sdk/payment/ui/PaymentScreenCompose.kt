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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sdk.payment.PaymentGateway
import com.sdk.payment.domain.model.BillingAddres
import com.sdk.payment.domain.model.CardDetails
import com.sdk.payment.domain.model.CustomerDetails
import com.sdk.payment.domain.model.ItemDetails
import com.sdk.payment.domain.model.PaymentDetails
import com.sdk.payment.domain.model.PaymentOptions
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.domain.model.ShippingAddres
import com.sdk.payment.nfc.NfcManager
import com.sdk.payment.presentation.BasePaymentViewModel
import com.sdk.payment.ui.component.AlertSecurity
import com.sdk.payment.ui.component.BottomSheetNfcScan
import com.sdk.payment.ui.component.CardHolderInput
import com.sdk.payment.ui.component.CardLogos
import com.sdk.payment.ui.component.CardNumberInput
import com.sdk.payment.ui.component.CardPreview
import com.sdk.payment.ui.component.CvvInfoDialog
import com.sdk.payment.ui.component.ExpiryCvvRow
import com.sdk.payment.ui.component.PayButton
import com.sdk.payment.ui.component.TapNfcInfo
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun PaymentScreen(
    token: String,
    jsonData: String,
) {
    val viewModel = remember(token, jsonData) {
        PaymentViewModel(token, jsonData)
    }
    val state = viewModel.state
    Box {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            CardPreview(state, viewModel)
            Spacer(Modifier.height(20.dp))
            TapNfcInfo {
                viewModel.showNfcSheet(true)
            }
            Spacer(Modifier.height(20.dp))
            CardNumberInput(state, viewModel)
            Spacer(Modifier.height(10.dp))
            CardLogos(state)
            Spacer(Modifier.height(10.dp))
            ExpiryCvvRow( state, viewModel,)
            Spacer(Modifier.height(20.dp))
            CardHolderInput(state, viewModel)
            Spacer(Modifier.height(20.dp))
            AlertSecurity()
            Spacer(Modifier.height(15.dp))
            PayButton()
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
    }
}
