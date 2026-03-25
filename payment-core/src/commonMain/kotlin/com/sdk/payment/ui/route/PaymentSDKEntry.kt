package com.sdk.payment.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sdk.payment.ui.PaymentScreen
import com.sdk.payment.ui.viewmodel.PaymentViewModel

@Composable
fun PaymentSDKNavHost(
    viewModel: PaymentViewModel,
    onResult: (Boolean) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
       navController = navController,
        startDestination = "payment"
    ) {
        composable("payment") {
            PaymentScreen(
              viewModel = viewModel
            )
        }
    }
}