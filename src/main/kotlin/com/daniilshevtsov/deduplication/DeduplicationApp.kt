package com.daniilshevtsov.deduplication

import com.daniilshevtsov.deduplication.di.AppComponent
import com.daniilshevtsov.deduplication.di.DaggerAppComponent
import com.daniilshevtsov.deduplication.feature.core.AppConfig
import javax.inject.Inject

class DeduplicationApp {
    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(
                appConfig = AppConfig(chunkSize = 8, storageDirectoryName = "storage", storageFileName = "storage.txt")
            )
    }

    @Inject
    lateinit var deduplicator: Deduplicator

    fun start(args: Array<String>) {
        appComponent.inject(this)
        deduplicator.run(args = args)
    }

}