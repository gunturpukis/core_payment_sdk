package com.sdk.payment.nfc

actual class NfcManager {
    actual fun startScan() {
    }

    actual fun stopScan() {
    }

    actual fun setListener(listener: (NfcResult) -> Unit) {
    }
}


//import platform.CoreNFC.*
//
//actual class NfcManager {
//
//    private var listener: NfcListener? = null
//
//    actual fun setListener(listener: NfcListener) {
//        this.listener = listener
//    }
//
//    actual fun startScan() {
//
//        val session = NFCNDEFReaderSession(
//            delegate = null,
//            queue = null,
//            invalidateAfterFirstRead = true
//        )
//
//        session.beginSession()
//    }
//
//    actual fun stopScan() {
//
//    }
//}