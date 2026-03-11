package com.sdk.payment.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
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

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: PaymentPageBinding
    private lateinit var viewModel: BasePaymentViewModel
    private lateinit var gateway: PaymentGateway
    private lateinit var lottieAnimation: LottieAnimationView

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
        val btnCvvInfo = binding.btnInfoCvv
        btnCvvInfo.setOnClickListener {
            showCvvDialog()
        }
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.layoutTap.setOnClickListener {
            lottieAnimation = binding.lottieTapCard
            lottieAnimation.speed = 1.0f
            lottieAnimation.playAnimation()
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

    fun showCvvDialog() {
        val builder = AlertDialog.Builder(this, androidx.constraintlayout.widget.R.style.AlertDialog_AppCompat_Light)
        val view = layoutInflater.inflate(R.layout.idalog_cvv_info, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        view.findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        window?.let {
            val layoutParams = it.attributes
            layoutParams.gravity = Gravity.CENTER
            val displayMetrics = resources.displayMetrics
            layoutParams.width = (displayMetrics.widthPixels * 0.9).toInt()
            it.attributes = layoutParams
            it.setDimAmount(0.7f)
        }
        dialog.show()
    }

    private fun setupGateway() {
        val mId = intent.getStringExtra("merchantId") ?: ""
        val sUnbound = intent.getStringExtra("secretUnbound") ?: ""
        val hKey = intent.getStringExtra("hashKey") ?: ""

        gateway = PaymentGateway(
            engine = OkHttp.create(),
            config = PaymentConfig(
                baseUrl = "https://api-stage.mcpayment.id/card-v2/v1/charge",
                merchantId = mId,
                timeoutMillis = 30_000,
                secretUnbound = sUnbound,
                hashKey = hKey,
                apiVersion = "v3",
//                externalId = "",
//                orderId = ""
            ),
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
        binding.etExpDate.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != current) {
                    val clean = s.toString().replace("[^\\d]".toRegex(), "")
                    val sel = binding.etExpDate.selectionStart
                    var formatted = ""
                    if (clean.length >= 2) {
                        formatted = clean.substring(0, 2) + "/" + clean.substring(2)
                    } else {
                        formatted = clean
                    }
                    if (formatted.length > 5) formatted = formatted.substring(0, 5)
                    current = formatted
                    binding.etExpDate.setText(formatted)
                    binding.etExpDate.setSelection(if (sel <= formatted.length) sel else formatted.length)
                    viewModel.onExpiryDateChange(formatted)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.tvCvvPreview.text =
            state.cvv.ifEmpty { "***" }

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