// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.android.application") version "8.5.0" apply false
  id("com.android.library") version "8.5.0" apply false
  id("org.jetbrains.kotlin.android") version "2.1.0" apply false
  id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
}
