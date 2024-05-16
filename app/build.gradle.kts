plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.viewnext"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.viewnext"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.text.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation("com.google.firebase:firebase-config-ktx:21.6.3")
    implementation (libs.retrofit)
    implementation (libs.material.v140)
    kapt (libs.androidx.room.compiler)
    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("androidx.activity:activity-ktx:1.3.1")

    // RetroMock
    implementation (libs.retrofit2.retrofit.mock)
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation (libs.infinum.retromock)
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    testImplementation("org.mockito:mockito-core:3.12.4")
    implementation ("androidx.compose.ui:ui:1.6.7")
    implementation ("androidx.compose.material:material:1.6.7")
    implementation ("androidx.activity:activity-compose:1.9.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation ("androidx.navigation:navigation-compose:2.7.7")


}
