package com.sdk.payment

import kotlin.experimental.ExperimentalObjCName

import platform.UIKit.UIViewController
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("PaymentSDK")
class PaymentSDK {

    companion object {
        fun shared(): PaymentSDK = PaymentSDK()
    }

    @ObjCName("createViewController")
    fun createViewController(): UIViewController {
        return MainViewController(
//            viewModel = TODO(),
            token = TODO(),
            json = TODO(),
            onResult = TODO()
        )
    }
}