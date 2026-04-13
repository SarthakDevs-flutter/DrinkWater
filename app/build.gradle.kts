plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.trending.water.drinking.reminder"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.trending.water.drinking.reminder"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//Third party library
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.bumptech.glide:glide:5.0.5")
    implementation("com.tbuonomo:dotsindicator:5.1.0")
//    api("com.theartofdev.edmodo:android-image-cropper:2.8.0")

//    Ads Service LIB
    implementation("com.google.android.gms:play-services-ads:25.1.0")
    implementation(libs.play.services.ads.api)
//    implementation("com.google.android.gms:play-services-ads:25.1.0")
    implementation("com.google.android.ump:user-messaging-platform:4.0.0")

//    Animation library
    implementation("com.airbnb.android:lottie:6.7.1")

//    General Library
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("jp.co.cyberagent.android:gpuimage:2.1.0")
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")
}