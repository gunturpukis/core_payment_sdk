package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CustomerInfo(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null
)