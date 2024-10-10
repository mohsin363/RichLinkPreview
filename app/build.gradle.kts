plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mohsin.richlinkpreview.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mohsin.richlinkpreview.app"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":RichLinkPreview"))
    implementation(libs.appcompat)
    implementation(libs.material)
}