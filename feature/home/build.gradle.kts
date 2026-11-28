plugins {
    alias(libs.plugins.quicktube.kmp.feature)
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(projects.core.download)
        implementation(projects.core.common)
        implementation(projects.core.datastore)
    }
}
