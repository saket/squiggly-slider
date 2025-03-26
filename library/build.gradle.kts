plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.mavenPublish)
  alias(libs.plugins.paparazzi)
  alias(libs.plugins.poko)
  alias(libs.plugins.compose.compiler)
}

android {
  namespace = "me.saket.squiggles"

  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()
    compileSdk = libs.versions.compileSdk.get().toInt()
  }
  buildFeatures {
    compose = true
  }
  lint {
    abortOnError = true
  }
}

dependencies {
  api(libs.compose.ui)
  api(libs.compose.foundation)
  api(libs.compose.material3)

  testImplementation(libs.junit)
}

// Used on CI to prevent publishing of non-snapshot versions.
tasks.register("throwIfVersionIsNotSnapshot") {
  val libraryVersion = properties["VERSION_NAME"] as String
  check(libraryVersion.endsWith("SNAPSHOT")) {
    "Project isn't using a snapshot version = $libraryVersion"
  }
}
