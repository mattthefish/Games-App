plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "1.8.22-1.0.11"
}

kotlin {
    sourceSets {
        getByName("debug") {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        getByName("release") {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

android {
    namespace = "com.example.gamesApp"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 33
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    implementation("io.github.raamcosta.compose-destinations:core:1.8.42-beta")
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.material3.android)
    ksp(libs.ksp.v1842beta)

    implementation(libs.androidx.compose.bom.v20230300)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.coil.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    androidTestImplementation(platform(libs.compose.bom.v20230300))
    androidTestImplementation(libs.ui.test.junit4)

    implementation(libs.androidx.compiler)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.activity.compose.v172)
    debugImplementation(libs.ui.tooling)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    androidTestImplementation(libs.androidx.navigation.testing)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
}
