plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.navigation.safe.args)
//    alias(libs.plugins.google.services)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}
android {
    namespace = "org.bohdan.mallproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.bohdan.mallproject"
        minSdk = 28
        targetSdk = 34
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
    testOptions{
        unitTests{

        }
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Google-Firebase
    implementation(platform(libs.firebase.bom))

    //Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    //Coroutine for FireStore (HZ CHY NADA)
    implementation(libs.kotlinx.coroutines.play.services)
    //auth
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.android.gms:play-services-base:18.5.0")

//    implementation(libs.gms.play.services.gcm)

    //firestore
//    implementation(libs.firebase.firestore.ktx)
    implementation("com.google.firebase:firebase-firestore")

    implementation("com.google.android.gms:play-services-base")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.androidx.junit.ktx)
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    //BottomSheetFragment
    implementation("com.google.android.material:material:1.9.0")

    //hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    //messaging
    implementation("com.google.firebase:firebase-messaging")

    //testing
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.androidx.espresso.core.v350)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.runner)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}