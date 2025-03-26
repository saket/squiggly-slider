plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "me.saket.squiggles.sample"

  defaultConfig {
    applicationId = namespace
    minSdk = 31
    compileSdk = libs.versions.compileSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }
  buildFeatures {
    compose = true
  }
  lint {
    abortOnError = true
  }
}

dependencies {
  implementation(projects.library)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.activity)
  implementation(libs.compose.foundation)
  implementation(libs.compose.ui)
  implementation(libs.compose.materialIcons)
  implementation(libs.blurhash)
}
