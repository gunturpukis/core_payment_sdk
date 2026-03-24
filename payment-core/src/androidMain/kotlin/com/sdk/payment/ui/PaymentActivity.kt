@file:Suppress("DEPRECATION")

package com.sdk.payment.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.sdk.payment.PaymentGateway
import com.sdk.payment.R
import com.sdk.payment.config.PaymentConfig
import com.sdk.payment.databinding.PaymentPageBinding
import com.sdk.payment.domain.model.PaymentRequest
import com.sdk.payment.nfc.NfcManager
import com.sdk.payment.presentation.BasePaymentViewModel
import com.sdk.payment.presentation.PaymentUiState
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import androidx.core.graphics.drawable.toDrawable
import com.sdk.payment.ui.component.PaymentSuccessDialog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: PaymentPageBinding
    private lateinit var viewModel: BasePaymentViewModel
    private lateinit var uiState: PaymentUiState
    private lateinit var gateway: PaymentGateway
    private lateinit var nfcManager: NfcManager
    private var loadingDialog: Dialog? = null
    private lateinit var lottieAnimation: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PaymentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nfcManager = NfcManager(this)

        setupGateway()
        setupViewModel()
        setupInputListeners()
        observeViewModelState()
        setupCardFlip()
        setupPayButton()
        setupBottomSheet()
        setupCvvInfoButton()

//        binding.composeView.setContent {
//            PaymentSuccessDialog(
//                isVisible = uiState.showSuccessDialog,
//                onDone = {
//                    viewModel.onSuccessDone()
//                }
//            )
//        }

    }

    override fun onPause() {
        super.onPause()
        nfcManager.stopScan()
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val tag = intent?.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        tag?.let {
            nfcManager.onTagDiscovered(it)
        }
    }
    private fun setupGateway() {
        try {
            val credToken = intent.getStringExtra("credentialToken") ?: ""
            val decodedString = credToken.decodeBase64String()
            val parts = decodedString.split(":")

            val merchantId = parts.getOrNull(0) ?: ""
            val secretUnbound = parts.getOrNull(1) ?: ""
            val hashKey = parts.getOrNull(2) ?: ""

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
        } catch (e: Exception) {
            showErrorToast("Failed to setup payment gateway: ${e.message}")
        }
    }
    private fun setupViewModel() {
        try {
            val paymentRequest = getPaymentRequest()
            viewModel = BasePaymentViewModel(gateway, paymentRequest)
        } catch (e: Exception) {
            showErrorToast("Failed to setup: ${e.message}")
        }
    }
    private fun getPaymentRequest(): PaymentRequest {
        val json = intent.getStringExtra("Payment_Data")
            ?: throw IllegalArgumentException("Payment_Data is missing")
        return Json.decodeFromString(json)
    }
    private fun setupInputListeners() {
        binding.etCardNumber.addTextChangedListener { text ->
            val input = text.toString()
            viewModel.onCardNumberChange(input)
        }
        binding.etCardName2.addTextChangedListener { text ->
            val input = text.toString()
            viewModel.onCardHolderChange(input)
        }
        binding.etExpDate.addTextChangedListener { text ->
            val input = text.toString()
            viewModel.onExpiryDateChange(input)
        }
        binding.etCVV.addTextChangedListener { text ->
            val input = text.toString()
            viewModel.onCvvChange(input)
        }
    }
    private fun observeViewModelState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateCardPreview(state)
                updateValidationErrors(state)

                state.errorMessage?.let { errorMsg ->
                    if (errorMsg != "Please fix the errors above") {
                        showErrorToast(errorMsg)
                    }
                }
                if (state.paymentResponse != null) {
                  lifecycleScope.launch {
                        delay(1500)
                        resetPaymentForm()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isLoading.collect { loading ->
                if (loading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }
    }
    private fun updateCardPreview(state: PaymentUiState) {
        binding.tvCardNumber.text = state.cardNumberFormatted.ifEmpty { "**** **** **** ****" }
        binding.tvNamePreview.text = state.cardHolder.ifEmpty { "CARD HOLDER" }
        binding.tvExpDate.text = state.expiryDate.ifEmpty { "MM/YY" }
        binding.tvCvvPreview.text = state.cvv.ifEmpty { "***" }
        updateCardTypeIcons(state.cardType.toString())
    }
    private fun updateCardTypeIcons(cardType: String) {
        val cardViewMappings = listOf(
            binding.ivvisa to "VISA",
            binding.ivmastercard to "MASTERCARD",
            binding.ivamex to "AMEX",
            binding.ivjcb to "JCB",
            binding.ivunion to "UNIONPAY"
        )

        cardViewMappings.forEach { (view, type) ->
            val isActive = cardType.equals(type, ignoreCase = true)
            view.alpha = if (isActive) 1.0f else 0.21f
            view.imageAlpha = if (isActive) 255 else (255 * 0.5).toInt()
        }
    }
    private fun updateValidationErrors(state: PaymentUiState) {
        updateFieldError(
            binding.tilCardNumber,
            binding.lblCardNumber,
            state.cardNumberError
        )
        updateFieldError(
            binding.tilCardName2,
            binding.lblCardNumber2,
            state.cardHolderError
        )
        updateFieldError(
            binding.tilExpDate,
            binding.lblExpDate,
            state.expiryDateError
        )
        updateFieldErrorWithIcon(
            binding.tilCvv,
            binding.lblCvv,
            binding.btnInfoCvv,
            state.cvvError
        )
    }
    private fun updateFieldError(
        textInputLayout: TextInputLayout,
        label: TextView,
        error: String?
    ) {
        textInputLayout.error = error
        label.setTextColor(if (error != null) Color.RED else Color.DKGRAY)
    }
    private fun updateFieldErrorWithIcon(
        textInputLayout: TextInputLayout,
        label: TextView,
        iconButton: ImageView,
        error: String?
    ) {
        updateFieldError(textInputLayout, label, error)
        iconButton.setColorFilter(if (error != null) Color.RED else Color.DKGRAY)
    }
    private fun setupBottomSheet() {
        val bottomSheet = binding.bottomSheetScan
        val blurView = binding.viewBlurBackground
        val sheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.layoutTap.setOnClickListener {
            startNfcScan(sheetBehavior)
        }
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                blurView.visibility = when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> View.INVISIBLE
                    BottomSheetBehavior.STATE_EXPANDED -> View.VISIBLE
                    else -> View.INVISIBLE
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                blurView.alpha = slideOffset
            }
        })
    }

    private fun startNfcScan(sheetBehavior: BottomSheetBehavior<View>) {
        nfcManager.startScan()
        nfcManager.setListener { result ->
            viewModel.onCardNumberChange(result.cardNumber.toString())
            viewModel.onExpiryDateChange(result.expiryDate.toString())
            viewModel.onCardHolderChange(result.cardHolder.toString())

            binding.etCardNumber.setText(result.cardNumber)
            binding.etExpDate.setText(result.expiryDate)
            binding.etCardName2.setText(result.cardHolder)

            nfcManager.stopScan()
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        lottieAnimation = binding.lottieTapCard
        lottieAnimation.speed = 1.0f
        lottieAnimation.playAnimation()

        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    private fun setupCardFlip() {
        binding.etCVV.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                flipToBack()
            } else {
                flipToFront()
            }
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
            viewModel.processPayment(lifecycleScope)
        }
    }
    private fun setupCvvInfoButton() {
        binding.btnInfoCvv.setOnClickListener {
            showCvvDialog()
        }
    }
    private fun showCvvDialog() {
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
    private fun showLoading() {
        if (loadingDialog == null || !loadingDialog!!.isShowing) {
            loadingDialog = Dialog(this)
            loadingDialog?.setContentView(R.layout.dialog_loading)
            loadingDialog?.setCancelable(false)
            loadingDialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            loadingDialog?.show()
        }
    }
    private fun hideLoading() {
        loadingDialog?.dismiss()
    }
    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    private fun showSuccessToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun resetPaymentForm() {
        viewModel.reset()
        binding.etCardNumber.text = null
        binding.etCardName2.text = null
        binding.etExpDate.text = null
        binding.etCVV.text = null
        viewModel.clearErrors()
    }
}