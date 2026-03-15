package com.sdk.payment.presentation.detector

import com.sdk.payment.domain.model.CardType

object CardTypeDetector {
    fun detect(cardNumber: String): CardType {
        val number = cardNumber.replace(" ", "")
        if (number.isEmpty()) return CardType.UNKNOWN
        return when {
            number.startsWith("4") ->
                CardType.VISA
            number.matches(Regex("^5[1-5].*")) ->
                CardType.MASTERCARD
            number.matches(Regex("^2(2[2-9][0-9]|[3-6][0-9]{2}|7[01][0-9]|720).*")) ->
                CardType.MASTERCARD
            number.startsWith("34") ||
                    number.startsWith("37") ->
                CardType.AMEX
            number.startsWith("6011") ||
                    number.startsWith("65") ->
                CardType.DISCOVER
            number.matches(Regex("^35(2[89]|[3-8][0-9]).*")) ->
                CardType.JCB
            number.startsWith("62") ||
                    number.startsWith("81") ->
                CardType.UNIONPAY
            else ->
                CardType.UNKNOWN
        }
    }
}