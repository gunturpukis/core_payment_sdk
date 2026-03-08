package com.sdk.payment.ui

import android.R.attr.alpha
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sdk.payment.PaymentGateway
import com.sdk.payment.R
import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.databinding.PaymentPageBinding
import com.sdk.payment.domain.model.CardType
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.presentation.BasePaymentViewModel
import com.sdk.payment.presentation.PaymentUiState
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.ZoneOffset

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

        val bottomSheet = binding.bottomSheetScan
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.layoutTap.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } }

        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState){
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })



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

//        when(state.cardType) {
//            CardType.VISA ->
//                binding.ivvisa.apply {
//                    setImageResource(R.drawable.visa).apply {
//                        imageAlpha = 255
//                        alpha = 1.0f
//                    }
//                }
//            CardType.MASTERCARD ->
//                binding.ivmastercard.apply {
//                setImageResource(R.drawable.payment_logo).apply {
//                    imageAlpha = 255
//                    alpha = 1.0f
//                }
//            }
//
//            CardType.AMEX ->
//                 binding.ivamex.apply {
//                    setImageResource(R.drawable.amex).apply {
//                        imageAlpha = 255
//                        alpha = 1.0f
//                    }
//                }
//            CardType.JCB ->
//                binding.ivjcb.apply {
//                setImageResource(R.drawable.jcb).apply {
//                    imageAlpha = 255
//                    alpha = 1.0f
//
//                }
//            }
//            else ->
//                binding.ivunion.apply {
//                    setImageResource(R.drawable.unionpay).apply {
//                        imageAlpha = 255
//                        alpha = 1.0f
//                    }
//                }
//
//        }

        val cardViews = listOf(binding.ivvisa, binding.ivmastercard, binding.ivamex, binding.ivjcb, binding.ivunion)
        cardViews.forEach {
            it.alpha = 0.21f
            it.imageAlpha = (255 * 0.5).toInt()
        }
        val activeView = when(state.cardType) {
            CardType.VISA -> binding.ivvisa
            CardType.MASTERCARD -> binding.ivmastercard
            CardType.AMEX -> binding.ivamex
            CardType.JCB -> binding.ivjcb
            CardType.UNIONPAY -> binding.ivunion
            else -> binding.ivmastercard
        }
        activeView.apply {
            alpha = 1.0f
            imageAlpha = 255
        }
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