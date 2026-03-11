package com.sdk.payment.foundation.network


import io.ktor.client.plugins.api.*
import io.ktor.http.content.TextContent
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.kotlincrypto.hash.sha2.SHA256

fun SignaturePlugin(hashKey: String) = createClientPlugin("SignaturePlugin") {
    onRequest { request, body ->
        if (body is TextContent) {
            val json = Json.parseToJsonElement(body.text).jsonObject
            val extId = json["external_id"]?.jsonPrimitive?.content ?: ""
            val orderId = json["order_id"]?.jsonPrimitive?.content ?: ""
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