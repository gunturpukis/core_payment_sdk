import org.gradle.kotlin.dsl.cocoapods
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.native.cocoapods")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.android.library")
    id("org.jetbrains.compose")
}

group = "com.company.sdk"
version = "1.0.0"

kotlin {
    sourceSets.all {
        languageSettings.optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
    }
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.all {
            freeCompilerArgs += listOf("-Xdisable-phases=Devirtualization")
//            binaryOptions["memoryModel"] = "experimental"
//            binaryOptions["lto"] = "none"
        }
    }
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvmToolchain(17)

    androidTarget()

    val paymentCoreXCFramework = XCFramework("PaymentGatewaySDK")

    iosX64 {
        binaries.framework {
            baseName = "PaymentGatewaySDK"
            isStatic = true
            freeCompilerArgs += listOf(
                "-Xbinary=stripDebugInfo=true"
            )
            paymentCoreXCFramework.add(this)
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = "PaymentGatewaySDK"
            isStatic = true
            freeCompilerArgs += listOf(
                "-Xbinary=stripDebugInfo=true"
            )
            paymentCoreXCFramework.add(this)
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = "PaymentGatewaySDK"
            isStatic = true
            freeCompilerArgs += listOf(
                "-Xbinary=stripDebugInfo=true"
            )
            paymentCoreXCFramework.add(this)
        }
    }
    cocoapods {
        summary = "Payment Gateway SDK"
        homepage = "https://example.com"
        ios.deploymentTarget = "14.0"
        framework {
            baseName = "PaymentGatewaySDK"
            isStatic = true
        }
//        pod("lottie-ios") {
//            version = "4.3.0"
//        }
        podfile = project.file("../iosApp/Podfile")
        name = "PaymentGatewaySDKPod"
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                val ktorVersion = "2.3.7"
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
//                implementation("io.github.alexzhirkevich:compottie:1.0.0-alpha01")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
                implementation("org.kotlincrypto.hash:sha2:0.5.1")
                implementation("io.github.aakira:napier:2.6.1")
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation("org.jetbrains.compose.material:material-icons-extended:1.6.10")
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")

            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
                implementation("com.google.android.material:material:1.11.0")
                implementation("com.airbnb.android:lottie:6.4.0")
//                implementation("io.github.alexzhirkevich:compottie:1.0.0-alpha02")
                implementation("io.github.aakira:napier:2.6.1")
                implementation("com.airbnb.android:lottie-compose:6.4.0")
                implementation("androidx.savedstate:savedstate")
                implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate")
                implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation("org.jetbrains.compose.material:material-icons-extended:1.6.10")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                // ✅ TAMBAHKAN INI agar iOS punya engine network-nya sendiri
                implementation("io.ktor:ktor-client-darwin:2.3.7")
            }
        }
        iosX64Main.dependsOn(iosMain)
        iosArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Main.dependsOn(iosMain)
    }
}

compose {
    resources {
        publicResClass = true
        packageOfResClass = "com.sdk.payment"
    }
}


android {
    namespace = "com.sdk.payment"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    buildFeatures {
            viewBinding = true
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    buildTypes {
        release {
            isMinifyEnabled = true
            consumerProguardFiles("consumer-rules.pro")
            proguardFiles("proguard-rules.pro")
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
//tasks.register("assembleSdk") {
//    group = "sdk"
//    description = "Assemble Android AAR and iOS XCFramework"
//    dependsOn(
//        "assembleRelease",
//        "assemblePaymentCoreSDKReleaseXCFramework"
//    )
//}

tasks.register("assembleSdk") {
    group = "sdk"
    description = "Assemble Android AAR and iOS XCFramework"
    dependsOn(
        "assembleRelease",
        "assemblePaymentGatewaySDKReleaseXCFramework" // Sesuaikan nama di sini
    )
}

tasks.register<Copy>("exportIosXCFramework") {
    group = "sdk"
    description = "Export iOS XCFramework to dist/ios"
    dependsOn("assemblePaymentGatewaySDKReleaseXCFramework") // Sesuaikan nama di sini
    from(layout.buildDirectory.dir("XCFrameworks/release"))
    into(layout.projectDirectory.dir("../dist/ios"))
}
//tasks.register<Copy>("exportIosXCFramework") {
//    group = "sdk"
//    description = "Export iOS XCFramework to dist/ios"
//    dependsOn("assemblePaymentCoreSDKReleaseXCFramework")
//    from(layout.buildDirectory.dir("XCFrameworks/release"))
//    into(layout.projectDirectory.dir("../dist/ios"))
//}

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
tasks.withType<Copy>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}