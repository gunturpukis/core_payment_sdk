package com.sdk.payment.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.sdk.payment.PaymentGateway
import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.databinding.PaymentPageBinding
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.presentation.BasePaymentViewModel
import com.sdk.payment.presentation.PaymentUiState
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: PaymentPageBinding
    private lateinit var viewModel: BasePaymentViewModel
    private lateinit var gateway: PaymentGateway

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaymentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGateway()
        setupViewModel()
        setupInputs()
        observeState()
        setupCardFlip()
        setupPayButton()
    }

    private fun setupGateway() {
        val mId = intent.getStringExtra("merchantId") ?: ""
        val sUnbound = intent.getStringExtra("secretUnbound") ?: ""
        val hKey = intent.getStringExtra("hashKey") ?: ""

        gateway = PaymentGateway(
            engine = OkHttp.create(),
            config = PaymentConfig(
                baseUrl = "https://api-stage.mcpayment.id",
                merchantId = mId,
                timeoutMillis = 30_000,
                secretUnbound = sUnbound,
                hashKey = hKey,
                refreshUrl = "",
                apiVersion = "v3"
            )
        )
    }

    private fun setupViewModel() {
        val paymentRequest = getPaymentRequest()
        viewModel = BasePaymentViewModel(gateway, paymentRequest)
    }
    private fun getPaymentRequest(): PaymentRequest {
        val json = intent.getStringExtra("Payment_Data")
            ?: error("Payment_Data is missing")
        return Json.decodeFromString(json)
    }

    private fun setupInputs() {
        binding.etCardNumber.addTextChangedListener {
            viewModel.onCardNumberChange(it.toString())
        }
        binding.etCardName2.addTextChangedListener {
            viewModel.onCardHolderChange(it.toString())
        }
        binding.etExpDate.addTextChangedListener {
            viewModel.onExpiryDateChange(it.toString())
        }
        binding.etCVV.addTextChangedListener {
            viewModel.onCvvChange(it.toString())
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                render(state)
            }
        }
    }

    private fun render(state: PaymentUiState) {
        updateCardPreview(state)
        state.errorMessage?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        if (state.isSuccess) {
            Toast.makeText(this, "Credit Card Valid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCardPreview(state: PaymentUiState) {
        binding.tvCardNumber.text =
            state.cardNumber.chunked(4).joinToString(" ")
                .ifEmpty { "**** **** **** ****" }
        binding.tvNamePreview.text =
            state.cardHolder.ifEmpty { "CARD HOLDER" }
        binding.tvExpDate.text =
            state.expiryDate.ifEmpty { "MM/YY" }
        binding.tvCvvPreview.text =
            state.cvv.ifEmpty { "***" }
    }
    private fun setupCardFlip() {
        binding.etCVV.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) flipToBack() else flipToFront()
        }
    }
    private fun flipToBack() {
        binding.cardPreviewFront.animate()
            .rotationY(90f)
            .setDuration(200)
            .withEndAction {
                binding.cardPreviewFront.visibility = View.GONE
                binding.cardPreviewBack.visibility = View.VISIBLE
                binding.cardPreviewBack.rotationY = -90f
                binding.cardPreviewBack.animate()
                    .rotationY(0f)
                    .setDuration(200)
                    .start()
            }.start()
    }
    private fun flipToFront() {
        binding.cardPreviewBack.animate()
            .rotationY(90f)
            .setDuration(200)
            .withEndAction {
                binding.cardPreviewBack.visibility = View.GONE
                binding.cardPreviewFront.visibility = View.VISIBLE
                binding.cardPreviewFront.rotationY = -90f
                binding.cardPreviewFront.animate()
                    .rotationY(0f)
                    .setDuration(200)
                    .start()
            }.start()
    }

    private fun setupPayButton() {
        binding.btnPay.setOnClickListener {
            lifecycleScope.launch {
                viewModel.processPayment()
            }
        }
    }
}