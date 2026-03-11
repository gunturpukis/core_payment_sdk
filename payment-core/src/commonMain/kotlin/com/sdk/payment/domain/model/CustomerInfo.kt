package com.sdk.payment.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerDetails(
    val email: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    val phone: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null,
)