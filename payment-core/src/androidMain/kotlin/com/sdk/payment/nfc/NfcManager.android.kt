package com.sdk.payment.nfc

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag

actual class NfcManager(private val activity: Activity){
    private var listener: NfcListener? = null
    private val adapter: NfcAdapter? =
        NfcAdapter.getDefaultAdapter(activity)

    actual fun startScan() {
        if (adapter == null){
            listener?.onError("NFC is not available on this device")
            return
        }
        if (!adapter.isEnabled){
            listener?.onError("NFC is disabled on this device")
            return
        }
    }

    actual fun stopScan() {
    }

    fun handleIntent(intent: Intent) {
        val tag: Tag? =
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        tag?.let {
            val id = it.id.joinToString("") {
                    byte -> "%02X".format(byte)
            }
            listener?.onTagDiscovered(
                NfcTag(
                    id = id,
                    payload = it.id
                )
            )
        }
    }

    actual fun setListener(listener: NfcListener) {
        this.listener = listener
    }
}