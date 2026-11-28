plugins {
    alias(libs.plugins.quicktube.kmp.feature)
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.download)
        implementation(projects.core.common)
    }
    sourceSets.androidMain.dependencies {
        implementation(libs.androidx.activity.compose)
    }
}
