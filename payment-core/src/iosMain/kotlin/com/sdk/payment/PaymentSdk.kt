package com.sdk.payment

import platform.UIKit.UIViewController
import kotlin.experimental.ExperimentalObjCName
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
            token = TODO(),
            json = TODO()
        )
    }
}