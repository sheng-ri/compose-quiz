plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.jetbrains.kotlin.serialization)
  alias(libs.plugins.devtoolsKsp)
}

android {
  namespace = "cn.hellozjf.project.composequiz"
  compileSdk = 36

  defaultConfig {
    applicationId = "cn.hellozjf.project.composequiz"
    minSdk = 26
    targetSdk = 36
    versionCode = 26
    versionName = "1.0.26"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }

  // TODO 我把阿里云机器翻译库导入之后，会有报错，需要添加以下依赖，否则app跑不起来，后面要搞明白这有啥用
  packaging {
    resources {
      pickFirst("META-INF/DEPENDENCIES")
      pickFirst("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
    }
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material.icons.extended)
  implementation(libs.androidx.material3)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.navigation3.ui)
  implementation(libs.kotlinx.serialization.core)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.androidx.material3.adaptive.navigation.suite)
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.runtime.livedata)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.ui.tooling)
  annotationProcessor(libs.androidx.room.room.compiler)
  ksp(libs.androidx.room.room.compiler)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
  implementation(libs.selenium.java)
  implementation(libs.pdfbox)
  implementation(libs.poi.core)
  implementation(libs.poi.ooxml)
  implementation(libs.commons.csv)
  implementation(libs.alimt)
  implementation(libs.tea.openapi)
}