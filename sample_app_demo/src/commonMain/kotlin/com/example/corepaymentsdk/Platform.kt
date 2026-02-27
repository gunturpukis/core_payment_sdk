package com.example.corepaymentsdk

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform