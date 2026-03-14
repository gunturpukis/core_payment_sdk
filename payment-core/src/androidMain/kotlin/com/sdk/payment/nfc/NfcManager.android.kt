package com.sdk.payment.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.sdk.payment.nfc.emv.EmvReader

actual class NfcManager(private val activity: Activity) {

    private var listener: ((NfcResult) -> Unit)? = null
    private val adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)
    actual fun setListener(listener: (NfcResult) -> Unit) {
        this.listener = listener
    }

    actual fun startScan() {
        val intent = Intent(activity, activity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            activity,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )
        adapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            null,
            null
        )
    }

    actual fun stopScan() {
        adapter?.disableForegroundDispatch(activity)
    }

    fun onTagDiscovered(tag: Tag) {
        val isoDep = IsoDep.get(tag) ?: return
        isoDep.connect()
        val transceiver = AndroidIsoDepTransceiver(isoDep)
        val reader = EmvReader(transceiver)
        val result = reader.read()
        listener?.invoke(result)
        isoDep.close()
    }
}