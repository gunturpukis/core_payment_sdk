package com.sdk.payment.nfc

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep

actual class NfcManager(private val activity: Activity) {

    private var listener: ((NfcResult) -> Unit)? = null
    private val adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)

    actual fun setListener(listener: (NfcResult) -> Unit) {
        this.listener = listener
    }

    actual fun startScan() {
        // enable NFC foreground
    }

    actual fun stopScan() {
        // disable NFC foreground
    }

    fun onTagDiscovered(tag: Tag) {

        val isoDep = IsoDep.get(tag) ?: return

        isoDep.connect()

        val reader = EmvReader(isoDep)

        val result = reader.read()

        listener?.invoke(result)

        isoDep.close()
    }
}