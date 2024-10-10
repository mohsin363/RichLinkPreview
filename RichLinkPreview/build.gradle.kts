import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    id("com.vanniktech.maven.publish") version "0.29.0"
    signing
}

android {
    namespace = "com.mohsin.richlinkpreview"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.picasso)
    implementation(libs.jsoup)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

group = "com.mohsin.richlinkpreview"
version = "1.0.0"

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true,
        )
    )

    coordinates("io.github.mohsin363", "richlinkpreview", "1.0.0")

    pom {
        name.set("RichLinkPreview")
        description.set("A Rich Link Preview Library for Android")
        inceptionYear.set("2024")
        url.set("https://github.com/mohsin363/RichLinkPreview")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("mohsin363")
                name.set("Mohsin")
                email.set("mohsinhassan63@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:https://github.com/mohsin363/RichLinkPreview.git")
            developerConnection.set("scm:git:ssh://git@github.com:mohsin363/RichLinkPreview.git")
            url.set("https://github.com/mohsin363/RichLinkPreview")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}

signing {
    useGpgCmd()
}
