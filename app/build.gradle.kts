plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tranconghaoit.homemanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tranconghaoit.homemanager"
        minSdk = 24
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.privacysandbox.tools:tools-core:1.0.0-alpha06")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    /* Add Library */
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.android.volley:volley:1.2.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    /* Call API */
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")

    implementation ("org.greenrobot:eventbus:3.2.0")

    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    /* PDF and Print*/
    implementation("com.itextpdf:itext7-core:7.1.15")
}