import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("com.android.library")
}

group = "com.company.sdk"
version = "1.0.0"

kotlin {

    jvmToolchain(17)

    androidTarget()

//    iosX64()
//    iosArm64()
//    iosSimulatorArm64()


    val paymentCoreXCFramework = XCFramework("PaymentCoreSDK")

    iosX64 {
        binaries.framework {
            baseName = "PaymentCoreSDK"
            isStatic = true
            paymentCoreXCFramework.add(this)
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = "PaymentCoreSDK"
            isStatic = true
            paymentCoreXCFramework.add(this)
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = "PaymentCoreSDK"
            isStatic = true
            paymentCoreXCFramework.add(this)
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.3.7")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
                implementation("io.ktor:ktor-client-auth:2.3.7")
                implementation("com.soywiz.korlibs.krypto:krypto:4.0.10")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.3.7")
            }
        }
    }
}

android {
    namespace = "com.sdk.payment"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }
    android {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}
tasks.register("buildSdk") {
    group = "sdk"
    description = "Build Payment SDK for Android and iOS"

    dependsOn(
        // Android
        "compileDebugKotlinAndroid",
        "compileReleaseKotlinAndroid",

        // iOS
        "compileKotlinIosArm64",
        "compileKotlinIosX64",
        "compileKotlinIosSimulatorArm64"
    )
}

tasks.register("cleanBuildSdk") {
    group = "sdk"
    description = "Clean & build Payment SDK for Android and iOS"

    dependsOn(
        "clean",
        "buildSdk"
    )
}
tasks.register("assembleSdk") {
    group = "sdk"
    description = "Assemble Android AAR and iOS XCFramework"

    dependsOn(
        "assembleRelease",
        "assemblePaymentCoreSDKReleaseXCFramework"
    )
}
tasks.register<Copy>("exportIosXCFramework") {
    group = "sdk"
    description = "Export iOS XCFramework to dist/ios"

    dependsOn("assemblePaymentCoreSDKReleaseXCFramework")

    from(layout.buildDirectory.dir("XCFrameworks/release"))
    into(layout.projectDirectory.dir("../dist/ios"))
}

tasks.register<Copy>("exportAndroidAar") {
    group = "sdk"
    description = "Export Android AAR to dist/android"

    dependsOn("assembleRelease")

    from(layout.buildDirectory.dir("outputs/aar"))
    into(layout.projectDirectory.dir("../dist/android"))
}

tasks.register("exportSdk") {
    group = "sdk"
    description = "Build & export Android + iOS SDK"

    dependsOn(
        "assembleSdk",
        "exportIosXCFramework",
        "exportAndroidAar"
    )
}
