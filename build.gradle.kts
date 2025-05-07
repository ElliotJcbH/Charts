//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    kotlin("plugin.serialization") version "1.9.22" apply false // Replace with your Kotlin version
//}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // Update the Kotlin version here
    kotlin("plugin.serialization") version "2.0.0" apply false // Use 2.0.0 or the latest stable
}