import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
}

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

android {
    namespace = "org.blueclub"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.blueclub"
        minSdk = 28
        targetSdk = 33
        versionCode = 2
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String","KAKAO_NATIVE_KEY",properties.getProperty("KAKAO_NATIVE_KEY"))
        buildConfigField("String","NAVER_CLIENT_ID",properties.getProperty("NAVER_CLIENT_ID"))
        buildConfigField("String","NAVER_CLIENT_SECRETE",properties.getProperty("NAVER_CLIENT_SECRETE"))
        buildConfigField("String","BC_BASE_URL",properties.getProperty("BC_BASE_URL"))
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.4.0-alpha01")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Ktx
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha04")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Coil
    implementation("io.coil-kt:coil:2.4.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")
    kapt("com.google.dagger:hilt-compiler:2.47")
    kapt("com.google.dagger:dagger-compiler:2.47")

    // Network
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    implementation("com.google.code.gson:gson:2.10")

    // Splash
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Kakao
    implementation("com.kakao.sdk:v2-user:2.12.0")

    // Naver
    implementation("com.navercorp.nid:oauth:5.6.0")

    // Lottie
    implementation("com.airbnb.android:lottie:6.3.0")

    // Indicator
    implementation("com.github.zhpanvip:viewpagerindicator:1.2.3")

    // Calendar
    implementation("com.kizitonwose.calendar:view:2.4.1")

    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
}