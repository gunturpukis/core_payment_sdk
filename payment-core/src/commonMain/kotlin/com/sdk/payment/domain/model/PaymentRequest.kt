package com.sdk.payment.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PaymentRequest(
    @SerialName("external_id")
    val externalId: String,
    @SerialName("order_id")
    val orderId: String,
    val currency: String,
    val source: String,
    @SerialName("payment_method")
    val paymentMethod: String? = null,
    @SerialName("payment_channel")
    val paymentChannel: String? = null,
    @SerialName("payment_mode")
    val paymentMode: String? = null,
    @SerialName("payment_details")
    val paymentDetails: PaymentDetails,
    @SerialName("item_details")
    val itemDetails: List<ItemDetails>? = null,
    @SerialName("customer_details")
    val customerDetails: CustomerDetails? = null,
    @SerialName("billing_address")
    val billingAddress: BillingAddres? = null,
    @SerialName("shipping_address")
    val shippingAddress: ShippingAddres? = null,
    @SerialName("card_details")
    val cardDetails: CardDetails? = null,
    @SerialName("return_url")
    val returnUrl: String? = null,
    @SerialName("callback_url")
    val callbackUrl: String? = null,
    @SerialName("payment_options")
    val paymentOptions: PaymentOptions,
    @SerialName("additional_data")
    val additionalData: String? = null
)

@Serializable
data class PaymentDetails(
    val amount: Long?,
    @SerialName("is_customer_paying_fee")
    val isCustomerPayingFee: Boolean,
    @SerialName("transaction_description")
    val transactionDescription: String? = null,
    @SerialName("expired_time")
    val expiredTime: String? = null
)

@Serializable
data class CardDetails(
    @SerialName("card_number")
    val cardNumber: String,
    @SerialName("card_expired_month")
    val cardExpiredMonth: String,
    @SerialName("card_expired_year")
    val cardExpiredYear: String,
    @SerialName("card_cvn")
    val cardCvn: String,
    @SerialName("card_holder_name")
    val cardHolderName: String
)
@Serializable
data class ItemDetails(
    @SerialName("item_id")
    val itemId: String,
    val name: String? = null,
    val amount: Int,
    val qty: Int,
    val description: String? = null
)
@Serializable
data class BillingAddres(
    @SerialName("full_name")
    val fullName: String,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null,
    val country: String? = null
)
@Serializable
data class ShippingAddres(
    @SerialName("full_name")
    val fullName: String,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    @SerialName("postal_code")
    val postalCode: String? = null,
    val country: String? = null
)
@Serializable
data class PaymentOptions(
    @SerialName("use_rewards")
    val useRewards: Boolean,
    @SerialName("campaign_code")
    val campaign_code: String? = null,
    val tenor: String? = null
)