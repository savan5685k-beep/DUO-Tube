plugins {
    alias(libs.plugins.quicktube.kmp.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android { withHostTest {} }
    sourceSets.commonMain.dependencies {
        api(projects.core.model)
        api(projects.core.common)
        implementation(projects.core.database)
        implementation(projects.core.datastore)
        implementation(projects.core.extractor)
        implementation(libs.kotlinx.serialization.json)
    }
    sourceSets.desktopMain.dependencies {
        implementation(libs.okhttp)
    }
    sourceSets.getByName("androidHostTest").dependencies {
        implementation(libs.junit)
    }
}
