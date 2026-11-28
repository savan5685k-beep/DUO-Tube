package com.edureminder.quicktube.core.analytics

import org.koin.dsl.module

/** Privacy fork: analytics and crash reporting are always disabled. */
actual val analyticsModule: org.koin.core.module.Module = module {
    single<Analytics> { NoOpAnalytics }
}
