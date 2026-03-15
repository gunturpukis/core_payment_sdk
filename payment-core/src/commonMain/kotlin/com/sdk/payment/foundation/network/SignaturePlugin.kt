package com.sdk.payment.foundation.network


import com.sdk.payment.domain.model.PaymentRequest
import io.ktor.client.plugins.api.*
import io.ktor.http.content.TextContent
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.kotlincrypto.hash.sha2.SHA256
val PaymentRequestAttribute = AttributeKey<PaymentRequest>("PaymentRequest")
fun SignaturePlugin(hashKey: String) = createClientPlugin("SignaturePlugin") {
    onRequest { request, _ ->
        val paymentRequest = request.attributes.getOrNull(PaymentRequestAttribute)
        if (paymentRequest != null) {
            val extId = paymentRequest.externalId
            val orderId = paymentRequest.orderId

            if (extId.isNotEmpty() && orderId.isNotEmpty()) {
                val dataToHash = "$hashKey$extId$orderId"
                val sha256 = SHA256()
                sha256.update(dataToHash.encodeToByteArray())
                val signature = sha256.digest().toHexString()
                request.headers.append("x-req-signature", signature)
            }
        }
    }
}
private fun ByteArray.toHexString(): String = joinToString("") {
    it.toUByte().toString(16).padStart(2, '0')
}