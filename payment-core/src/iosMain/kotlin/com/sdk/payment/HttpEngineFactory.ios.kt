package com.sdk.payment

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual fun getHttpEngine(): HttpClientEngine = Darwin.create()