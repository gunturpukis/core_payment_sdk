package com.sdk.payment.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.sdk.payment.nfc.NfcListener
import com.sdk.payment.nfc.NfcManager
import com.sdk.payment.nfc.NfcTag
import com.sdk.payment.presentation.BasePaymentViewModel
import com.sdk.payment.presentation.PaymentUiState
import io.github.aakira.napier.Napier
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import androidx.core.graphics.drawable.toDrawable
import com.sdk.payment.nfc.EmvNfcReader

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: PaymentPageBinding
    private lateinit var viewModel: BasePaymentViewModel
    private lateinit var gateway: PaymentGateway
    private lateinit var lottieAnimation: LottieAnimationView
    private lateinit var nfcManager: NfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaymentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nfcManager = NfcManager(this)

        setupGateway()
        setupViewModel()
        setupInputs()
        observeState()
        setupCardFlip()
        setupPayButton()
        setUpsheetBottom()
        setupNfcManager()

        val btnCvvInfo = binding.btnInfoCvv
        btnCvvInfo.setOnClickListener {
            showCvvDialog()
        }
    }
    private var loadingDialog: Dialog? = null
    private fun showLoading() {
        loadingDialog = Dialog(this)
        loadingDialog?.setContentView(R.layout.dialog_loading)
        loadingDialog?.setCancelable(false)
        loadingDialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        loadingDialog?.show()
    }
    private fun hideLoading() {
        loadingDialog?.dismiss()
    }

    fun setupNfcManager() {
        nfcManager.setListener(object : NfcListener{
            override fun onTagDiscovered(tag: NfcTag) {
                Napier.d(tag.id)
            }
            override fun onError(error: String) {
                Napier.e(error)
            }
        })
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        nfcManager.handleIntent(intent)
    }
    fun setUpsheetBottom(){
        val bottomSheet = binding.bottomSheetScan
        val blurView = binding.viewBlurBackground
        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.layoutTap.setOnClickListener {

            nfcManager.setListener(object : NfcListener{
                override fun onTagDiscovered(tag: NfcTag) {
                    lifecycleScope.launch {
                           val cardData = EmvNfcReader.readCard(
                               tag = tag
                           )
//
//                        runOnUiThread {
//
//                            binding.etCardNumber.setText(cardData.cardNumber)
//                            binding.etExpDate.setText(cardData.expiryDate)
//                            binding.etCardName2.setText(cardData.cardHolder)
//
//                            viewModel.onCardNumberChange(cardData.cardNumber)
//                            viewModel.onExpiryDateChange(cardData.expiryDate)

//                        }
                    }
                }
                override fun onError(error: String) {
                }

            })

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
                        blurView.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        blurView.visibility = View.GONE
                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                blurView.alpha = slideOffset
            }
        })
    }

    fun showCvvDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.idalog_cvv_info, null)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            val params = attributes
            params.gravity = Gravity.CENTER
            val displayMetrics = resources.displayMetrics
            params.width = (displayMetrics.widthPixels * 0.9).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.6f)
        }
        view.findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setupGateway() {
        val credToken = intent.getStringExtra("credentialToken") ?: ""
        val decodedString = credToken.decodeBase64String()
        val parts = decodedString.split(":")
            val merchantId = parts[0]
            val secretUnbound = parts[1]
            val hashKey = parts[2]
            println("Merchant ID: $merchantId")
            println("SecretOunbond: $secretUnbound")
            println("hashKey: $hashKey")

        gateway = PaymentGateway(
            engine = OkHttp.create(),
            config = PaymentConfig(
                baseUrl = "https://api-stage.mcpayment.id/card-v2/v1/charge",
                merchantId = merchantId,
                timeoutMillis = 30_000,
                secretUnbound = secretUnbound,
                hashKey = hashKey,
                apiVersion = "v3",
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
            val cardNumber = it.toString()
            viewModel.onCardNumberChange(cardNumber)
            if (cardNumber.replace(" ", "").length < 16) {
                binding.tilCardNumber.error = "Card number must be at least 16 digits"
                binding.lblCardNumber.setTextColor(Color.RED)
            } else {
                binding.tilCardNumber.error = null
                binding.lblCardNumber.setTextColor(Color.DKGRAY)
            }
        }
        binding.etCardName2.addTextChangedListener {
            val cardName = it.toString()
            viewModel.onCardHolderChange(cardName)
            if (cardName.isEmpty()){
                binding.tilCardName2.error = "Card holder name cannot be empty"
                binding.lblCardNumber2.setTextColor(Color.RED)
            } else {
                binding.tilCardName2.error = null
                binding.lblCardNumber2.setTextColor(Color.DKGRAY)
            }
        }
        binding.etExpDate.addTextChangedListener {
            val expiryDate = it.toString()
            viewModel.onExpiryDateChange(it.toString())
            if (expiryDate.isEmpty()) {
                binding.tilExpDate.error = "Expired date cannot be empty"
                binding.lblExpDate.setTextColor(Color.RED)
            } else {
                binding.tilExpDate.error = null
                binding.lblExpDate.setTextColor(Color.DKGRAY)
            }
        }
        binding.etCVV.addTextChangedListener {
            val cvv = it.toString()
            viewModel.onCvvChange(it.toString())
            if(cvv.isEmpty()){
                binding.tilCvv.error = "CVV cannot be empty"
                binding.lblCvv.setTextColor(Color.RED)
                binding.btnInfoCvv.setColorFilter(Color.RED)
            } else {
                binding.tilCvv.error = null
                binding.lblCvv.setTextColor(Color.DKGRAY)
                binding.btnInfoCvv.setColorFilter(Color.DKGRAY)
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                render(state)
                state.errorMessage?.let { message ->
                    Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { loading ->
                if (loading) {
                    showLoading()
                } else {
                    hideLoading()
                }
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