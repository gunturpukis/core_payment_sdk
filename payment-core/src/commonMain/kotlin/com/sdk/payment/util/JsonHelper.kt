package com.sdk.payment.util

import com.sdk.payment.domain.model.PaymentRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonHelper {
    private val jsonFormatter = Json {
        encodeDefaults = false
        explicitNulls = false
        ignoreUnknownKeys = true
    }
    fun toJson(request: PaymentRequest): String {
        return jsonFormatter.encodeToString(request)
    }
}