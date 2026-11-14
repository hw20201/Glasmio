plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}
android {
    namespace = "io.github.neoruaa.glasmio.navdemo"
    compileSdk = 37
    defaultConfig {
        applicationId = "io.github.neoruaa.glasmio.navdemo"
        minSdk = 33
        targetSdk = 37
        versionCode = 1
        versionName = "0.1.3"
    }
    buildFeatures { compose = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
}
dependencies {
    implementation(project(":glasmio-ui"))
    implementation(project(":glasmio-scaffold"))
    implementation(project(":glasmio-icons"))
    implementation("androidx.activity:activity-compose:1.13.0")
}
